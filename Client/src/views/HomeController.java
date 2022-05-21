package views;

import Apps.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Partie;
import utils.CallbackInstance;

/**
 * The type Home controller.
 */
public class HomeController extends CallbackInstance {

    private MainApp mainApp;
    private int dimensionX = 10;
    private int dimensionY = 10;
    private Partie partieCreated;


    @FXML
    private TableView<Partie> partiesEnCoursTableView;

    @FXML
    private TableColumn<Partie, Integer> identifiantTableColumn;

    @FXML
    private TableColumn<Partie, Integer> nbPlayerTableColumn;

    @FXML
    private TableColumn<Partie, String> dimensionTableColumn;

    @FXML
    private Button rejoindrePartieButton;

    @FXML
    private Button quitterButton;

    @FXML
    private Button creationPartieButton;

    @FXML
    private Label dimensionsLabel;

    @FXML
    private Label nbFatomesLabel;

    @FXML
    private Spinner<Integer> nombreDeJoueursMaxSpinner;

    @FXML
    private Spinner<Integer> dimensionsXSpinner;

    @FXML
    private Spinner<Integer> dimensionsYSpinner;

    @FXML
    private Spinner<Integer> nombreDeFantomesSpinner;

    @FXML
    private Label partiesOuvertesLabel;

    @FXML
    private Label creationPartiesLabel;

    @FXML
    private Label nombreDeJoueursMaxLabel;

    @FXML
    private Label notGoodServerWarningLabel;

    /**
     * Sets main app, and fills UI with parties list and config
     *
     * @param mainApp the main app
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        this.mainApp.getConnectionHandler().registerCallback("REGNO", this, CallbackInstance::regno);
        partiesEnCoursTableView.setItems(mainApp.getPartiesList());
        partiesOuvertesLabel.setText("Parties ouvertes à l'inscription sur "+mainApp.getServerConfig().getAdresseServeur()+":"+mainApp.getServerConfig().getPortServeur());
        creationPartiesLabel.setText("Créer une partie sur " + mainApp.getServerConfig().getAdresseServeur()+":"+mainApp.getServerConfig().getPortServeur());

        if (!mainApp.getServerConfig().isServeurAmeliore()){
            nombreDeJoueursMaxSpinner.setDisable(true);
            nombreDeJoueursMaxLabel.setDisable(true);
            dimensionsXSpinner.setDisable(true);
            dimensionsYSpinner.setDisable(true);
            dimensionsLabel.setDisable(true);
            nombreDeFantomesSpinner.setDisable(true);
            nbFatomesLabel.setDisable(true);
            creationPartieButton.setDisable(true);
            notGoodServerWarningLabel.setVisible(true);


        } else {
            nombreDeJoueursMaxSpinner.setDisable(false);
            nombreDeJoueursMaxLabel.setDisable(false);
            dimensionsXSpinner.setDisable(false);
            dimensionsYSpinner.setDisable(false);
            dimensionsLabel.setDisable(false);
            nombreDeFantomesSpinner.setDisable(false);
            nbFatomesLabel.setDisable(false);
            creationPartieButton.setDisable(false);
            notGoodServerWarningLabel.setVisible(false);
        }
    }

    /**
     * Instantiates a new Home controller.
     */
    public HomeController() {
    }
    
    @FXML
    private void initialize() {
        // Cette methode est appelée automatiquement par JavaFX lors de la création de la fénêtre.
        // Les instructions permettent d'affecter les propriétés de l'élément Parties à chaque colonne du tableView
        partiesEnCoursTableView.setPlaceholder(new Label("Aucune partie n'est actuellement disponible"));
        identifiantTableColumn.setCellValueFactory(cellData -> cellData.getValue().identifiantProperty().asObject());
        nbPlayerTableColumn.setCellValueFactory(cellData -> cellData.getValue().nbPlayersProperty().asObject());
        dimensionTableColumn.setCellValueFactory(cellData -> cellData.getValue().dimensionsProperty());
    }

    @FXML
    private void handleQuitButtonClick(){
        // Cette methode est appelée lorsque l'on clique sur le bouton quitter. Ce comportement est défini dans le fichier FXML
        try {
            this.mainApp.quitMainScreen();
        } catch (Exception e1){
            e1.printStackTrace();
        }
    }

    @FXML
    private void handlejoinButtonClick() {
        // Cette méthode est appelée lorsque l'on clique sur le bouton rejoindre la partie. Comprtement défini dans le fichier XML
        try {
            this.mainApp.getConnectionHandler().registerCallback("REGOK", this, CallbackInstance::partieCreationCallback);
            Partie selectedPartie = partiesEnCoursTableView.getSelectionModel().getSelectedItem();
            if (selectedPartie!=null) {
                mainApp.getConnectionHandler().getWriter().send("REGIS ")
                        .send(this.mainApp.getServerConfig().getUsername() + " ")
                        .send(String.valueOf(this.mainApp.getConnectionHandler().getMessageriePrivee().getPort()))
                        .send(" ")
                        .send((byte) selectedPartie.getIdentifiant())
                        .end();
                //this.mainApp.joinGameLobby(selectedPartie);
                System.out.println(this.mainApp.getConnectionHandler().getSocketPort());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    private void handleDefaultGameButtonClick() {
        this.mainApp.getConnectionHandler().registerCallback("REGOK", this, CallbackInstance::partieCreationCallback);
        partieCreated = new Partie(-1, 1, true);
        mainApp.getConnectionHandler().getWriter()
                .send("NEWPL ")
                .send(this.mainApp.getServerConfig().getUsername() + " ")
                .send(String.valueOf(this.mainApp.getConnectionHandler().getMessageriePrivee().getPort()))
                .end();
    }

    @FXML
    private void handleCreateGameButtonClick(){
        this.mainApp.getConnectionHandler().registerCallback("REGOK", this, CallbackInstance::partieCreationCallback);
        partieCreated = new Partie(-1, 1, true);
        mainApp.getConnectionHandler().getWriter()
                .send("CUSPL ")
                .send(this.mainApp.getServerConfig().getUsername() + " ")
                .send(this.mainApp.getConnectionHandler().getMessageriePrivee().getPort() + " ")
                .send((byte) (int) this.dimensionsXSpinner.getValue())
                .send(" ")
                .send((byte) (int) this.dimensionsYSpinner.getValue())
                .send(" ")
                .send((byte) (int) this.nombreDeFantomesSpinner.getValue())
                .send(" ")
                .send((byte) (int) this.nombreDeJoueursMaxSpinner.getValue())
                .end();
    }

    @Override
    public void regno(String s) {
        Platform.runLater(() -> {
            // On crée un dialogue pour avertir l'utilisateur de son erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getConfigStage());
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de rejoindre la partie.");
            alert.setContentText("Veuillez réessayer, choisir une autre partie ou changer de nom d'utilisateur.");

            alert.showAndWait();
        });
    }

    @Override
    public void partieCreationCallback(String s) {
        this.mainApp.getConnectionHandler().releaseCallback("REGOK");
        int identifiant = Integer.parseInt(s.split(" ")[1]);
        if (this.partieCreated == null) {
            this.partieCreated = this.mainApp.getPartiesList().filtered(p -> p.getIdentifiant() == identifiant).get(0);
            this.partieCreated.setConnected(true);

        } else {
            this.partieCreated.setIdentifiant(identifiant);
            this.partieCreated.setConnected(true);
            this.mainApp.getPartiesList().add(this.partieCreated);
        }
        Platform.runLater(() -> {
            try {
                this.mainApp.joinGameLobby(this.partieCreated);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }
}
