package views;

import Apps.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import models.Partie;
import utils.CallbackInstance;

import java.util.ArrayList;


/**
 * The type Lobby controller.
 */
public class LobbyController extends CallbackInstance {

    private MainApp mainApp;

    private Partie partie;

    @FXML
    private Label statusLabel;

    @FXML
    private Button quitterButton;

    @FXML
    private Button lancerPartieButton;

    @FXML
    private ListView<String> playersInListView;


    @FXML
    private Label dimensionsLabel;
    // X et Y ?

    @FXML
    private Label nombreGhostLabel;


    @FXML
    private Label identifantLabel;

    @FXML
    private SplitPane lobbySplitPane;

    @FXML
    private AnchorPane leftAnchorPane;

    private ArrayList<String> playersRefusedToStartGame = new ArrayList<>();
    private boolean hasPressedStartButton = false;

    /**
     * Sets main app.
     *
     * @param mainApp the main app
     * @param p       the p
     */
    public void setMainApp(MainApp mainApp, Partie p) {
        this.mainApp = mainApp;
        this.partie = p;

        this.partie.getPlayersNames().clear();

        mainApp.getConnectionHandler().clearAll();

        mainApp.getConnectionHandler().registerCallback("PLJND", this, CallbackInstance::addPlayer); // ok
        mainApp.getConnectionHandler().registerCallback("REGOK", this, CallbackInstance::updateConnexionStatus, true); // ok
        mainApp.getConnectionHandler().registerCallback("PQUIT", this, CallbackInstance::removePlayer); // ok

        mainApp.getConnectionHandler().registerCallback("PSTAT", this, CallbackInstance::updateStartGameStatus); // ok

        mainApp.getConnectionHandler().registerCallback("WELCO", this, CallbackInstance::gameStart);

        mainApp.getConnectionHandler().registerCallback("SIZE!", this, CallbackInstance::updateGameDim);
        mainApp.getConnectionHandler().registerCallback("PLAYR", this, CallbackInstance::addPlayer);

        mainApp.getConnectionHandler().registerCallback("START", this, CallbackInstance::gameStart);

        if (!this.partie.isCreator())
            mainApp.getConnectionHandler().getWriter().send("REGIS ")
                    .send(this.mainApp.getServerConfig().getUsername() + " ")
                    .send("6942 ")
                    .send((byte) this.partie.getIdentifiant())
                    .end();


        System.out.println("ID : " + this.partie.getIdentifiant());
        mainApp.getConnectionHandler().getWriter()
                .send("SIZE? ")
                .send((byte) this.partie.getIdentifiant())
                .end();

        mainApp.getConnectionHandler().getWriter()
                .send("LIST? ")
                .send((byte) this.partie.getIdentifiant())
                .end();
        // initialiser la partie
        populateScreen();

    }

    @FXML
    private void initialize() {
        playersInListView.setMouseTransparent( true );
        playersInListView.setFocusTraversable( false );
    }

    private void populateScreen(){
        //modeJeuLabel.setText(this.partie.getModeDeJeu().equals("1") ? "Speeding contest" : this.partie.getModeDeJeu().equals("2") ? "Tour par tour" : "Brouillard de guerre");
        dimensionsLabel.setText(this.partie.getDimensions());
        nombreGhostLabel.setText(String.valueOf(this.partie.getNbGhosts()));
        //nombreTresorsLabel.setText(String.valueOf(this.partie.getNombreDeTresors()));
        playersInListView.setItems(partie.getPlayersNames());
        playersInListView.setCellFactory(stringListView -> new ColoredListCellFormat());
        lancerPartieButton.requestFocus();
        leftAnchorPane.maxWidthProperty().bind(lobbySplitPane.widthProperty().multiply(0.25));
        leftAnchorPane.minWidthProperty().bind(lobbySplitPane.widthProperty().multiply(0.25));
        identifantLabel.setText(String.valueOf(this.partie.getIdentifiant()));

        lancerPartieButton.setDisable(false);

        if (this.partie.isConnected()){
            this.updateConnexionStatus("REGOK");
        }

    }
    @FXML
    private void handleQuitButtonClick(){
        if(this.mainApp.getServerConfig().isServeurAmeliore()){
            this.mainApp.getConnectionHandler().getWriter().send("UNREG").end();
        }
        // Cette methode est appelée lorsque l'on clique sur le bouton quitter. Ce comportement est défini dans le fichier FXML
        try {
            this.partie.setCreator(false);
            this.partie.setConnected(false);
            this.mainApp.quitLobby();
        } catch (Exception e1){
            e1.printStackTrace();
        }
    }

    @Override
    public void addPlayer(String s) {
        if (this.partie.getPlayersNames().contains(s.split(" ")[1])) return;
        if (!this.playersRefusedToStartGame.contains(s.split(" ")[1])) this.playersRefusedToStartGame.add(s.split(" ")[1]);
        Platform.runLater(() -> {
            this.partie.getPlayersNames().add(s.split(" ")[1]);
            this.playersInListView.refresh();
        });
    }

    @Override
    public void removePlayer(String s) {
        if (!this.partie.getPlayersNames().contains(s.split(" ")[1])) return;
        Platform.runLater(() -> {
            this.partie.getPlayersNames().remove(s.split(" ")[1]);
            this.playersInListView.refresh();
        });
    }

    @Override
    public void updateConnexionStatus(String s) {
        System.out.println("Updating connection status...");
        if (s.split(" ")[0].equals("REGOK")){
            Platform.runLater(() -> {
                statusLabel.setText("Connecté");
                statusLabel.setTextFill(Color.web("#008000"));
            });
        } else if (s.split(" ")[0].equals("REGNO")){
            Platform.runLater(() -> {
                statusLabel.setText("Impossible de rejoindre la partie");
            });
        }
    }

    @FXML
    private void startGameButtonPressed(){
        hasPressedStartButton = true;
        this.lancerPartieButton.setDisable(true);
        this.quitterButton.setDisable(true);
        statusLabel.setText("En attente de confirmation des autres joueurs...");
        statusLabel.setTextFill(Color.web("#800000"));

        this.mainApp.getConnectionHandler().getWriter().send("START").end();
    }

    @Override
    public void updateStartGameStatus(String s) {
        this.playersRefusedToStartGame.remove(s.split(" ")[1]);
        this.playersInListView.refresh();
    }

    @Override
    public void gameStart(String s) {
        Platform.runLater(() -> {
            this.mainApp.startGame(this.partie);
        });
    }

    @Override
    public void updateGameDim(String s){
        int x = Integer.parseInt(s.split(" ")[2]);
        int y = Integer.parseInt(s.split(" ")[3]);
        this.partie.setDimensions(x, y);
        Platform.runLater(() -> {dimensionsLabel.setText(this.partie.getDimensions());});

    }

    /**
     * The type Colored list cell format.
     */
    public class ColoredListCellFormat extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(item);
            if (item == null) setStyle("");
            else setStyle("-fx-background-color: "+((playersRefusedToStartGame.contains(item))?"red;":"green;")+" -fx-text-fill: white;");
            //setBackground(new Background( new BackgroundFill(playersRefusedToStartGame.contains(item) ? Color.RED:Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}