package views;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Config;
import utils.MyPrintWriter;
import utils.MyScanner;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The type Param serveur controller.
 */
public class ParamServeurController {

    @FXML
    private TextField adresseServeurTextField;

    @FXML
    private Spinner<Integer> portServeurSpinner;

    @FXML
    private Button OKButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Button resetButton;

    @FXML
    private CheckBox serveurAmelioreCheckBox;

    private WelcomeController parentController;


    /**
     * Gets parent controller.
     *
     * @return the parent controller
     */
    public WelcomeController getParentController() {
        return parentController;
    }

    /**
     * Sets parent controller.
     *
     * @param parentController the parent controller
     */
    public void setParentController(WelcomeController parentController) {
        this.parentController = parentController;
        this.adresseServeurTextField.setText(parentController.getConfig().getAdresseServeur());
    }

    private boolean checkServerAddress(String url, int port){
        if (!serveurAmelioreCheckBox.isSelected()){
            return true;
        }
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(url, port), 3*1000);
            MyScanner scanner = new MyScanner(socket.getInputStream());
            scanner.useDelimiter("\\s*\\*{3}\\s*");
            MyPrintWriter printWriter = new MyPrintWriter(socket.getOutputStream(), true);

            printWriter.println("PING?");
            String rep = scanner.next();
            scanner.close();
            printWriter.close();
            socket.close();
            return rep.equals("PING!");
        } catch (Exception e){
            return false;
        }
    }

    @FXML
    private void handleOKButtonPressed(){
        // Cette méthode est appelée lorsque le bouton OK est pressé. Ce comportement est défini dans le fichier FXML
        if (checkServerAddress(adresseServeurTextField.getText(), portServeurSpinner.getValue())) {
            Config config = new Config();
            config.setAdresseServeur(adresseServeurTextField.getText());
            config.setPortServeur(portServeurSpinner.getValue());
            config.setServeurAmeliore(serveurAmelioreCheckBox.isSelected());
            this.parentController.setConfig(config);
            this.parentController.fermerParametresServeurFenetre();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(parentController.getMainApp().getConfigStage());
            alert.setTitle("Erreur");
            alert.setHeaderText("Aucun serveur ne répond à l'adresse séléctionée.");
            alert.setContentText("Vérifie que l'adresse et le port saisis sont corrects, puis vérifie que le serveur est actif.");

            alert.showAndWait();
        }

    }

    @FXML
    private void handleCancelButtonPressed(){
        // Cette méthode est appelée lorsque le bouton Annuler est pressé. Ce comportement est défini dans le fichier FXML
        this.parentController.fermerParametresServeurFenetre();
    }

    @FXML
    private void handleResetButtonPressed(){
        this.parentController.getConfig().resetServerConfig();
        this.adresseServeurTextField.setText(this.parentController.getConfig().getAdresseServeur());
        this.portServeurSpinner.getValueFactory().setValue(this.parentController.getConfig().getPortServeur());
    }
}
