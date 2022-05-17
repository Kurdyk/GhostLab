package Apps;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Config;
import models.Partie;
import utils.CallbackInstance;
import utils.ConnectionHandler;
import utils.PartiesUpdater;
import utils.RecurrentServerRequest;
import views.HomeController;
import views.LobbyController;
import views.WelcomeController;

import java.util.ArrayList;
import java.util.Timer;

/**
 * The type Main app.
 */
public class MainApp extends Application {

    // Fenetre principale
    private Stage primaryStage;

    // Fenetre de saisie du nom d'utilisateur
    private Stage configStage;

    private Stage lobbyStage;

    // La liste des parties en cours. On utilise pas une liste classique car celle-ci permet de faire en sorte que l'interface graphique se mette a jour automatiquement.
    private ObservableList<Partie> partiesList = FXCollections.observableArrayList();

    // La configuration du serveur (adresse, port, nom d'utilisateur)
    private Config serverConfig = new Config();

    private ConnectionHandler connectionHandler;

    private Timer fetchPartiesListTimer;

    private final PartiesUpdater partiesUpdater = new PartiesUpdater(this);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Font.loadFont(getClass().getResource("../Chilanka-Regular.ttf").toExternalForm(), 10);
        this.primaryStage = primaryStage;

        displayConfigStage();

    }

    /**
     * Instantiates a new Main app.
     */
    public MainApp(){
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (this.connectionHandler != null){
            this.connectionHandler.quitter();
        }
    }

    private void fetchPartiesList(){
        connectionHandler.registerCallback("GAMES", this.partiesUpdater, CallbackInstance::parse, true);
        connectionHandler.registerCallback("OGAME", this.partiesUpdater, CallbackInstance::parse, true);
        connectionHandler.registerCallback("SIZE!", this.partiesUpdater, CallbackInstance::parse, true);
        connectionHandler.registerCallback("LIST!", this.partiesUpdater, CallbackInstance::parse, true);

        if (this.serverConfig.isServeurAmeliore()) {
            fetchPartiesListTimer = connectionHandler.registerRecurrentServerCall(new RecurrentServerRequest() {
                @Override
                public void run() {
                    handler.getWriter().send("GAME?").end();
                }
            }, 500);
        }

    }

    public void updateParties(ArrayList<String> list){
        ArrayList<Integer> identifiants = new ArrayList<>();
        for (Partie partie : this.partiesList){
            identifiants.add(partie.getIdentifiant());
        }
        for (String message : list){
            String[] liste_commandes = message.split(" ");
            int id = Integer.parseInt(liste_commandes[1]);
            if (identifiants.contains(id)){
                identifiants.remove((Integer) id);
                connectionHandler.getWriter().send("SIZE? ").send((byte) id).end();
                connectionHandler.getWriter().send("LIST? ").send((byte) id).end();
            } else {
                Partie nouvelle_partie = new Partie(id, Integer.parseInt(liste_commandes[2]));
                partiesList.add(nouvelle_partie);
                connectionHandler.getWriter().send("SIZE? ").send((byte) id).end();
                connectionHandler.getWriter().send("LIST? ").send((byte) id).end();
            }
        }
        partiesList.removeIf(partie -> identifiants.contains(partie.getIdentifiant()));
    }



    private void resumeMainStageStartup(){
        try {
            // On nomme le stage.
            this.primaryStage.setTitle("GhostLab");
            this.primaryStage.getIcons().add(new Image("logo2.png"));
            // On crée un nouveau Loader et on lui donne le fichier fxml qui correspond à la fenêtre d'acceuil
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/home.fxml"));

            // On récupère le premier élément du XML. Il contient tous les autres. Ici c'est une AnchorPane (cf. views/hpme.fxml)
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // On crée une scène avec notre élément racine en paramètre
            Scene scene = new Scene(rootLayout);
            // On affecte la scène au stage principal et on l'affiche
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    connectionHandler.quitter();
                }
            });

            this.fetchPartiesList();

            // On récupère l'instance du controlleur du loader. Il est spécifié dans le fichier fxml
            HomeController controller = loader.getController();
            // On indique au controlleur quelle est l'application principale (pour le callback)
            controller.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayConfigStage() throws Exception{

        // Meme procédure que pour resumeMainStageStartup
        this.configStage = new Stage();
        this.configStage.setTitle("Configuration du jeu");
        this.configStage.getIcons().add(new Image("logo2.png"));

        FXMLLoader configLoader = new FXMLLoader();
        configLoader.setLocation(getClass().getResource("/views/welcome.fxml"));

        BorderPane configLayout = (BorderPane) configLoader.load();
        Scene configScene = new Scene(configLayout);

        this.configStage.setScene(configScene);
        this.configStage.setResizable(false);
        this.configStage.show();

        WelcomeController configController = configLoader.getController();
        configController.setMainApp(this);
        configController.setConfig(serverConfig);

    }


    private void displayLobbyStage(Partie p) throws Exception {
        fetchPartiesListTimer.cancel();
        this.lobbyStage = new Stage();
        this.lobbyStage.setTitle("Lobby");
        this.lobbyStage.getIcons().add(new Image("logo2.png"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/lobby.fxml"));

        AnchorPane rootLayout = (AnchorPane) loader.load();
        Scene scene = new Scene (rootLayout);

        this.lobbyStage.setScene(scene);
        this.lobbyStage.show();

        LobbyController controller = loader.getController();
        controller.setMainApp(this, p);

        this.configStage.close();
    }




    /**
     * Start game.
     *
     * @param p the p
     */


    public void startGame(Partie p){
        /*GameApp game = new GameApp(this, p);
        game.launch();
        this.lobbyStage.close();*/
    }


    /**
     * Game stage closed.
     */
    public void gameStageClosed(){
        this.resumeMainStageStartup();
    }


    /**
     * Create server connection.
     *
     * @param config the config
     */
    public void createServerConnection(Config config){
        this.connectionHandler = new ConnectionHandler(config);
        this.connectionHandler.start();
    }

    /**
     * Warn config done. Close config window
     *
     * @param config the config
     */
    public void warnConfigDone(Config config){
        // La configuration (serveur + username) est terminée. On ferme la fenetre de configuration et on affiche la fenêtre principale en sauvegardant la configuration
        this.serverConfig = config;
        this.configStage.close();
        this.resumeMainStageStartup();

    }

    /**
     * Quit main screen and reopen configuration screen.
     *
     * @throws Exception an exception if displayConfigStage fails
     */
    public void quitMainScreen() throws Exception{
        this.fetchPartiesListTimer.cancel();
        this.getPartiesList().clear();
        // Gère l'appui sur le bouton Quitter de l'inteface
        this.connectionHandler.quitter();
        this.primaryStage.close();
        //this.partiesList.clear();
        this.displayConfigStage();
    }

    /**
     * Quit lobby.
     *
     * @throws Exception the exception
     */
    public void quitLobby() throws Exception{
        this.lobbyStage.close();
        this.resumeMainStageStartup();
    }

    /**
     * Join game lobby.
     *
     * @param p the p
     * @throws Exception the exception
     */


    public void joinGameLobby(Partie p) throws Exception {
        // Gère l'appui sur le bouton Rejoindre la partie de l'interface
        this.primaryStage.close();
        this.displayLobbyStage(p);
    }




    // Getters & setters

    /**
     * Gets parties list.
     *
     * @return the parties list
     */

    public ObservableList<Partie> getPartiesList() {
        return partiesList;
    }



    /**
     * Gets primary stage.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Gets config stage.
     *
     * @return the config stage
     */
    public Stage getConfigStage() {
        return configStage;
    }

    /**
     * Gets server config.
     *
     * @return the server config
     */
    public Config getServerConfig() {
        return serverConfig;
    }

    /**
     * Gets connection handler.
     *
     * @return the connection handler
     */
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
