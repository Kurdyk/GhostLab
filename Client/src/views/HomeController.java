package views;

import Apps.MainApp;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private Button defaultGameButton;

    @FXML
    private ChoiceBox<String> modeDeJeuChoiceBox;

    @FXML
    private Spinner<Integer> nombreDeTresorsSpinner;

    @FXML
    private Spinner<Integer> nombreDeJoueursMaxSpinner;

    @FXML
    private Spinner<Integer> dimensionsXSpinner;

    @FXML
    private Spinner<Integer> dimensionsYSpinner;

    @FXML
    private Spinner<Integer> nombreDeTrousSpinner;

    @FXML
    private CheckBox robotsCheckBox;

    @FXML
    private Label partiesOuvertesLabel;

    @FXML
    private Label creationPartiesLabel;

    @FXML
    private Label nombreDeJoueursMaxLabel;

    @FXML
    private Label robotsLabel;

    @FXML
    private Label notGoodServerWarningLabel;

    /**
     * Sets main app, and fills UI with parties list and config
     *
     * @param mainApp the main app
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        partiesEnCoursTableView.setItems(mainApp.getPartiesList());
        partiesOuvertesLabel.setText("Parties ouvertes à l'inscription sur "+mainApp.getServerConfig().getAdresseServeur()+":"+mainApp.getServerConfig().getPortServeur());
        creationPartiesLabel.setText("Créer une partie sur " + mainApp.getServerConfig().getAdresseServeur()+":"+mainApp.getServerConfig().getPortServeur());

        if (!mainApp.getServerConfig().isServeurAmeliore()){
            nombreDeJoueursMaxSpinner.setDisable(true);
            robotsCheckBox.setDisable(true);
            nombreDeJoueursMaxLabel.setDisable(true);
            robotsLabel.setDisable(true);

        } else {
            notGoodServerWarningLabel.setVisible(false);
            modeDeJeuChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue){
                    if (newValue.equals("Speeding contest")){
                        robotsCheckBox.setSelected(false);
                        robotsCheckBox.setDisable(true);
                    } else {
                        robotsCheckBox.setDisable(false);
                    }
                }
            });
        }

        this.mainApp.getConnectionHandler().registerCallback("REGOK", this, CallbackInstance::partieCreationCallback);
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

        dimensionsXSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.dimensionX = newValue;
            updateValueFactories();


        });
        dimensionsYSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.dimensionY = newValue;
            updateValueFactories();

        });

        nombreDeTrousSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateValueFactories();
        });

        nombreDeTresorsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateValueFactories();
        });

    }

    private void updateValueFactories(){
        SpinnerValueFactory.IntegerSpinnerValueFactory trousFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3*this.dimensionY*dimensionX/25, nombreDeTrousSpinner.getValue() < 3*this.dimensionY*dimensionX/25 ? nombreDeTrousSpinner.getValue() : 3*this.dimensionY*dimensionX/25);
        nombreDeTrousSpinner.setValueFactory(trousFactory);

        SpinnerValueFactory.IntegerSpinnerValueFactory tresorsFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, (int) ((1.5*4*(dimensionX*dimensionY)/5-nombreDeTrousSpinner.getValue())/20), nombreDeTresorsSpinner.getValue() < (int) ((1.5*4*(dimensionX*dimensionY)/5-nombreDeTrousSpinner.getValue())/20) ? nombreDeTresorsSpinner.getValue() : (int) ((1.5*4*(dimensionX*dimensionY)/5-nombreDeTrousSpinner.getValue()))/20);
        nombreDeTresorsSpinner.setValueFactory(tresorsFactory);

        SpinnerValueFactory.IntegerSpinnerValueFactory nbPlayersFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,(4*(dimensionX*dimensionY)/5-nombreDeTrousSpinner.getValue()-nombreDeTresorsSpinner.getValue())/20, nombreDeJoueursMaxSpinner.getValue() < (4*(dimensionX*dimensionY)/5-nombreDeTrousSpinner.getValue()-nombreDeTresorsSpinner.getValue())/20 ? nombreDeJoueursMaxSpinner.getValue() : (4*(dimensionX*dimensionY)/5-nombreDeTrousSpinner.getValue()-nombreDeTresorsSpinner.getValue())/20);
        nombreDeJoueursMaxSpinner.setValueFactory(nbPlayersFactory);

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
            Partie selectedPartie = partiesEnCoursTableView.getSelectionModel().getSelectedItem();
            if (selectedPartie!=null) {
                this.mainApp.joinGameLobby(selectedPartie);
                System.out.println(this.mainApp.getConnectionHandler().getSocketPort());
                mainApp.getConnectionHandler().getWriter().send("REGIS ")
                        .send(this.mainApp.getServerConfig().getUsername() + " ")
                        .send("6942 ")
                        .send((byte) selectedPartie.getIdentifiant())
                        .end();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    private void handleDefaultGameButtonClick() {
        partieCreated = new Partie(-1, 1, true);
        mainApp.getConnectionHandler().getWriter()
                .send("NEWPL ")
                .send(this.mainApp.getServerConfig().getUsername() + " ")
                .send(String.valueOf(this.mainApp.getConnectionHandler().getSocketPort()))
                .end();
    }

    @FXML
    private void handleCreateGameButtonClick(){
        /*partieCreated = new Partie(-1, mainApp.getServerConfig().getUsername(), modeDeJeuChoiceBox.getValue().equals("Speeding contest") ? "1" : modeDeJeuChoiceBox.getValue().equals("Tour par tour") ? "2" : "3",
                dimensionsXSpinner.getValue(), dimensionsYSpinner.getValue(), nombreDeTrousSpinner.getValue(), nombreDeTresorsSpinner.getValue(), mainApp.getServerConfig().isServeurAmeliore() ? nombreDeJoueursMaxSpinner.getValue() : -1,
                mainApp.getServerConfig().isServeurAmeliore() && robotsCheckBox.isSelected());
        partieCreated.setCreator(true);

        mainApp.getConnectionHandler().registerCallback("111", this, CallbackInstance::partieCreationCallback);
        mainApp.getConnectionHandler().send(String.format("110 CREATE %s SIZE %d %d HOLE %d TRES %d" + (mainApp.getServerConfig().isServeurAmeliore() ? " PLAYERS "+partieCreated.getMaxPlayers()+" ROBOTS "+partieCreated.isRobots() : ""),
                partieCreated.getModeDeJeu(),
                partieCreated.getDimensionX(),
                partieCreated.getDimensionY(),
                partieCreated.getNombreDeTrous(),
                partieCreated.getNombreDeTresors()));
*/


    }


    @Override
    public void partieCreationCallback(String s) {
        int identifiant = Integer.parseInt(s.split(" ")[1]);
        if (this.partieCreated == null) {
            this.partieCreated = this.mainApp.getPartiesList().filtered(p -> p.getIdentifiant() == identifiant).get(0);
        } else {
            this.partieCreated.setIdentifiant(identifiant);
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
