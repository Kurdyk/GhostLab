package Apps;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Config;
import models.Partie;
import models.Plateau;
import utils.*;
import views.ChatController;
import views.LeaderBoardController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Game app.
 */
public class GameApp {
    /**
     * The Main app.
     */
    public MainApp mainApp;
    private Stage gameStage;
    private Stage leaderBoardStage;
    private Stage chatStage;
    private final Partie partie;
    private final Plateau plateau;
    /**
     * The Gc.
     */
    public GraphicsContext gc;
    private AnimationTimer timer;
    private final int screenWidth;
    private final int screenHeight;
    private final int COEFF_IMAGE;

    private final HashMap<KeyCode, Long> keyEvents = new HashMap<>();
    private final ArrayList<KeyCode> directions = new ArrayList<>();

    private double dragOffsetX;
    private double dragOffsetY;

    private final ObservableList<LeaderBoardItem> leaderBoardItems = FXCollections.observableArrayList(LeaderBoardItem.extractor());
    private final ObservableList<ChatItem> chatItems = FXCollections.observableArrayList(ChatItem.extractor());
    private final ArrayList<ChatItem> pendingMessages = new ArrayList<>();

    private LeaderBoardController leaderBoardController;
    private ChatController chatController;

    Timer fetchPlayersPositionsTimer;

    private final ConcurrentHashMap<ImageCrop, Long> haveToDrawOnTop = new ConcurrentHashMap<>();
    // private final Map<ImageCrop, Long> haveToDrawOnTop = new HashMap<>();



    /**
     * Instantiates a new Game app.
     *
     * @param mainApp the main app
     * @param partie  the partie
     */
    public GameApp(MainApp mainApp, Partie partie) {
        this.mainApp = mainApp;
        this.partie = partie;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenHeight = (int) (screenBounds.getHeight()*0.9);
        screenWidth = (int) (screenBounds.getWidth()*0.70);
        int sizeX = screenWidth/ partie.getDimensionX();
        int sizeY = screenHeight/partie.getDimensionY();
        COEFF_IMAGE = Math.min(sizeX, sizeY);
        this.plateau = new Plateau(partie.getDimensionX(), partie.getDimensionY(), COEFF_IMAGE, this);

        this.mainApp.getConnectionHandler().clearAll();
        mainApp.getConnectionHandler().registerCallback("GPLYR", plateau, CallbackInstance::updatePlayerPosition);
        mainApp.getConnectionHandler().registerCallback("MOVE!", plateau, CallbackInstance::handleMove);
        mainApp.getConnectionHandler().registerCallback("MOVEF", plateau, CallbackInstance::handleMoveCapture);
        mainApp.getConnectionHandler().registerCallback("GOBYE", plateau, CallbackInstance::partieFinie);
        mainApp.getConnectionHandler().registerCallback("MESSA", plateau, CallbackInstance::receivePublicMessage);
        mainApp.getConnectionHandler().registerCallback("MESSP", plateau, CallbackInstance::receivePrivateMessage);
        mainApp.getConnectionHandler().registerCallback("SEND!", plateau, CallbackInstance::messageSentConfirmed);
        mainApp.getConnectionHandler().registerCallback("NSEND", plateau, CallbackInstance::messageSentConfirmed);
        mainApp.getConnectionHandler().registerCallback("ENDGA", plateau, CallbackInstance::partieFinie);
        mainApp.getConnectionHandler().registerCallback("GHOST", plateau, CallbackInstance::ghostMove);
        mainApp.getConnectionHandler().registerCallback("SCORE", plateau, CallbackInstance::ghostCaptured);
        mainApp.getConnectionHandler().registerCallback("MUSIC", plateau, CallbackInstance::receiveMusic, true);





    }


    /**
     * Launch.
     */
    public void launch(){
        this.gameStage = new Stage();
        this.gameStage.getIcons().add(new Image("Fantomes_Logo.png"));
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + 5;
        double y = bounds.getMinY() + 5;
        System.out.println("X POS : "+x);
        System.out.println("Y POS : "+y);
        this.gameStage.setX(x);
        this.gameStage.setY(y);
        Group root = new Group();
        Scene gameScene = new Scene(root);
        gameStage.setScene(gameScene);


        Canvas canvas = new Canvas(partie.getDimensionX()*this.COEFF_IMAGE, partie.getDimensionY()*this.COEFF_IMAGE);
        root.getChildren().add(canvas);
        canvas.setFocusTraversable(true);

        this.gc = canvas.getGraphicsContext2D();

        double ratio = 528.0 / 522.0;
        int py = (int) (getScreenWidth() / ratio);
        int px = (int) (getScreenHeight() * ratio);
        int Sx, Sy;
        if (py < getScreenHeight()){
            Sx = px;
            Sy = getScreenHeight();
        } else {
            Sx = getScreenWidth();
            Sy = py;
        }

        timer = new AnimationTimer(){
            @Override
            public void handle(long l) {
                drawGame();
                drawGhosts();
                drawPlayers();
                drawOnTop();
            }
        };
        timer.start();

        root.setOnKeyPressed(this::handleKeyPressed);

        fetchPlayersPositionsTimer = this.mainApp.getConnectionHandler().registerRecurrentServerCall(new RecurrentServerRequest() {
            private final ConnectionHandler lock = mainApp.getConnectionHandler();
            @Override
            public void run() {
                synchronized (lock) {
                    handler.getWriter().send("GLIS?").end();
                }
            }
        }, 50);


        this.gameStage.setOnCloseRequest(windowEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quitter la partie ?");
            alert.setContentText("Une partie est en cours. Es-tu sûr de vouloir abandonner ?");
            alert.initOwner(gameStage.getOwner());
            alert.setHeaderText(null);
            Optional<ButtonType> res = alert.showAndWait();

            if (res.isPresent()){
                if (res.get().equals(ButtonType.CANCEL)){
                    windowEvent.consume();
                }
                else {
                    this.mainApp.getConnectionHandler().getWriter().send("IQUIT").end();
                    timer.stop();
                    fetchPlayersPositionsTimer.cancel();
                    leaderBoardStage.close();
                    chatStage.close();
                    this.releaseAllCallbacks();
                    mainApp.gameStageClosed();
                }
            }

        });



        this.leaderBoardStage = new Stage();
        this.leaderBoardStage.getIcons().add(new Image("Fantomes_Logo.png"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/leaderBoard.fxml"));
        AnchorPane rootPane = null;
        try {
            rootPane = loader.load();
        } catch (IOException e){
            e.printStackTrace();
        }
        assert rootPane != null;
        Scene scene = new Scene(rootPane, bounds.getWidth() * 0.15 - 5, partie.getDimensionY() * COEFF_IMAGE);

        this.leaderBoardStage.setScene(scene);
        this.leaderBoardStage.initStyle(StageStyle.UNDECORATED);
        this.leaderBoardStage.setX(x + partie.getDimensionX() * COEFF_IMAGE + 5);
        System.out.println("LEADERBOARD X POS : "+ (x + partie.getDimensionX()*this.COEFF_IMAGE + 5));
        //this.leaderBoardStage.setX(y);
        this.leaderBoardStage.setTitle("Leader Board");
        this.leaderBoardStage.setResizable(false);

        scene.setOnMousePressed(this::handleMousePressed);
        scene.setOnMouseDragged(this::handleMouseDragged);

        leaderBoardController = loader.getController();
        leaderBoardController.setGameApp(this);


        this.chatStage = new Stage();
        this.chatStage.getIcons().add(new Image("Fantomes_Logo.png"));
        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("/views/chat.fxml"));
        AnchorPane chatPane = null;
        try {
            chatPane = loader2.load();
        } catch (IOException e){
            e.printStackTrace();
        }
        assert chatPane != null;
        Scene chatScene = new Scene(chatPane, bounds.getWidth() * 0.15 - 5, partie.getDimensionY() * COEFF_IMAGE);

        this.chatStage.setScene(chatScene);
        this.chatStage.initStyle(StageStyle.UNDECORATED);
        this.chatStage.setX(x + (partie.getDimensionX() * COEFF_IMAGE + 5) + bounds.getWidth() * 0.15);
        this.chatStage.setTitle("Chat en direct");
        this.chatStage.setResizable(false);

        chatScene.setOnMousePressed(this::handleMousePressedChat);
        chatScene.setOnMouseDragged(this::handleMouseDraggedChat);

        chatController = loader2.getController();
        chatController.setGameApp(this);





        this.gameStage.setResizable(false);
        this.leaderBoardStage.show();
        this.chatStage.show();
        this.gameStage.show();
        int enc = (int) (this.gameStage.getHeight() - partie.getDimensionY()*this.COEFF_IMAGE);
        this.leaderBoardStage.setY(y + enc);
        this.chatStage.setY(y + enc);
    }

    private void releaseAllCallbacks(){
        this.mainApp.getConnectionHandler().clearAll(true);
    }

    private void processKeyEvent(KeyEvent keyEvent){
        System.out.println("TOUCHE PRESSEE : " + keyEvent.getCode().getName());
        this.directions.add(keyEvent.getCode());
        synchronized (this.mainApp.getConnectionHandler()) {
            switch (keyEvent.getCode()) {
                case UP -> this.mainApp.getConnectionHandler().getWriter().send("UPMOV 001").end();
                case DOWN -> this.mainApp.getConnectionHandler().getWriter().send("DOMOV 001").end();
                case LEFT -> this.mainApp.getConnectionHandler().getWriter().send("LEMOV 001").end();
                case RIGHT -> this.mainApp.getConnectionHandler().getWriter().send("RIMOV 001").end();
                default -> {
                }
            }
        }
    }


    private void handleKeyPressed(KeyEvent keyEvent) {
        long currentTime = System.currentTimeMillis();
        Long lastCall = this.keyEvents.get(keyEvent.getCode());
        if (lastCall == null || (currentTime - lastCall) > 50){
            this.keyEvents.put(keyEvent.getCode(), currentTime);
            this.processKeyEvent(keyEvent);
        } else {
            keyEvent.consume();
        }
    }


    /**
     * Draw game.
     */
    protected void drawGame() {
        for (int x = 0; x < partie.getDimensionX(); x++) {
            for (int y = 0; y < partie.getDimensionY(); y++) {
                gc.drawImage(plateau.getPlateau().get(x).get(y).getImageCase(), x * this.COEFF_IMAGE, y * this.COEFF_IMAGE);
            }
        }
    }

    /**
     * Draw players.
     */
    protected void drawPlayers() {
        for(String name : plateau.getCoordonneesJoueurs().keySet()) {
            String nameToDraw;
            nameToDraw = name;
            if (this.mainApp.getServerConfig().getUsername().equals(name)) {
                gc.drawImage(plateau.getListeImages().get(3), plateau.getCoordonneesJoueurs().get(name).getX() * COEFF_IMAGE, plateau.getCoordonneesJoueurs().get(name).getY() * COEFF_IMAGE);
                nameToDraw = "Moi";
            } else {
                gc.drawImage(plateau.getListeImages().get(4), plateau.getCoordonneesJoueurs().get(name).getX() * COEFF_IMAGE, plateau.getCoordonneesJoueurs().get(name).getY() * COEFF_IMAGE);
            }
            gc.setFill(new Color(0,0,0,0.3));
            Text t = new Text();
            t.setText(nameToDraw);
            t.setFont(Font.font("Chilanka Regular", 20));
            int sizeX = (int) (t.getLayoutBounds().getWidth());
            int sizeY = (int) (t.getLayoutBounds().getHeight());
            gc.fillRect((int) ((plateau.getCoordonneesJoueurs().get(name).getX()+0.5)*COEFF_IMAGE - sizeX/2 - 4) , (int) ((plateau.getCoordonneesJoueurs().get(name).getY())*COEFF_IMAGE - (sizeY/2.) - 22), sizeX+8, 4+sizeY);
            gc.setFont(Font.font("Chilanka Regular", 20));
            gc.setFill(new Color(1,1,1,1));
            gc.fillText(nameToDraw, (int) ((plateau.getCoordonneesJoueurs().get(name).getX()+0.5)*COEFF_IMAGE - sizeX/2), ((plateau.getCoordonneesJoueurs().get(name).getY())*COEFF_IMAGE) - 18 + 2);
        }
    }

    protected void drawGhosts(){
        for(Coordinates c: plateau.getCoordonneesGhosts().keySet()){
            if (plateau.getCoordonneesGhosts().get(c) < System.currentTimeMillis() - 10000){
                plateau.getCoordonneesGhosts().remove(c);
            } else {
                gc.drawImage(plateau.getListeImages().get(7), c.getX() * COEFF_IMAGE, c.getY() * COEFF_IMAGE);
            }
        }
    }

    public void registerDrawOnTop(ImageCrop imageCrop, long duration){
        System.out.println("On enregistre une demande de draw on top jusqua " + (System.currentTimeMillis() + duration) +" actuellement "+System.currentTimeMillis());
        this.haveToDrawOnTop.put(imageCrop, System.currentTimeMillis() + duration);
    }


    private void drawOnTop(){
        for (ImageCrop i : this.haveToDrawOnTop.keySet()){
            if (this.haveToDrawOnTop.get(i) > System.currentTimeMillis()){
                gc.drawImage(i.getImage(), i.getCropStartX(), i.getCropStartY(), i.getCropWidth(), i.getCropHeight(), 0, 0, getScreenWidth(), getScreenHeight());
            } else {
                this.haveToDrawOnTop.remove(i);
            }
        }
    }



    /**
     * Handle mouse pressed.
     *
     * @param e the e
     */
    protected void handleMousePressed(MouseEvent e)
    {
        this.dragOffsetX = e.getScreenX() - this.leaderBoardStage.getX();
        this.dragOffsetY = e.getScreenY() - this.leaderBoardStage.getY();
    }

    /**
     * Handle mouse dragged.
     *
     * @param e the e
     */
    protected void handleMouseDragged(MouseEvent e)
    {
        this.leaderBoardStage.setX(e.getScreenX() - this.dragOffsetX);
        this.leaderBoardStage.setY(e.getScreenY() - this.dragOffsetY);
    }


    /**
     * Handle mouse pressed.
     *
     * @param e the e
     */
    protected void handleMousePressedChat(MouseEvent e)
    {
        this.dragOffsetX = e.getScreenX() - this.chatStage.getX();
        this.dragOffsetY = e.getScreenY() - this.chatStage.getY();
    }

    /**
     * Handle mouse dragged.
     *
     * @param e the e
     */
    protected void handleMouseDraggedChat(MouseEvent e)
    {
        this.chatStage.setX(e.getScreenX() - this.dragOffsetX);
        this.chatStage.setY(e.getScreenY() - this.dragOffsetY);
    }

    /**
     * Gets directions.
     *
     * @return the directions
     */
    public ArrayList<KeyCode> getDirections() {
        return directions;
    }

    /**
     * Get server config config.
     *
     * @return the config
     */
    public Config getServerConfig(){
        return mainApp.getServerConfig();
    }

    /**
     * Get connection handler connection handler.
     *
     * @return the connection handler
     */
    public ConnectionHandler getConnectionHandler(){
        return mainApp.getConnectionHandler();
    }

    /**
     * Gets leader board items.
     *
     * @return the leader board items
     */
    public ObservableList<LeaderBoardItem> getLeaderBoardItems() {
        return leaderBoardItems;
    }

    public ObservableList<ChatItem> getChatItems() {
        return chatItems;
    }

    /**
     * Gets screen width.
     *
     * @return the screen width
     */
    public int getScreenWidth() {
        return COEFF_IMAGE * partie.getDimensionX();
    }

    /**
     * Gets screen height.
     *
     * @return the screen height
     */
    public int getScreenHeight() {
        return COEFF_IMAGE * partie.getDimensionY();
    }


    /**
     * Gets timer.
     *
     * @return the timer
     */
    public AnimationTimer getTimer() {
        return timer;
    }

    /**
     * Gets game stage.
     *
     * @return the game stage
     */
    public Stage getGameStage() {
        return gameStage;
    }

    /**
     * End game.
     */
    public void endGame(){
        leaderBoardStage.close();
        gameStage.close();
        fetchPlayersPositionsTimer.cancel();
        releaseAllCallbacks();
        mainApp.gameStageClosed();
    }


    /**
     * Gets partie.
     *
     * @return the partie
     */
    public Partie getPartie() {
        return partie;
    }

    public Timer getFetchPlayersPositionsTimer() {
        return fetchPlayersPositionsTimer;
    }

    /**
     * Gets plateau.
     *
     * @return the plateau
     */
    public Plateau getPlateau() {
        return plateau;
    }

    public ArrayList<ChatItem> getPendingMessages() {
        return pendingMessages;
    }
}
