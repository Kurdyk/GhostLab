package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The type Partie.
 */
public class Partie {

    private boolean creator = false;

    private final IntegerProperty identifiant;
    private final IntegerProperty nbPlayers;

    private StringProperty dimensions;

    private ObservableList<String> playersNames = FXCollections.observableArrayList();

    private IntegerProperty nbGhosts = new SimpleIntegerProperty();

    private boolean connected = false;


    public Partie(int identifiant, int nbPlayers) {
        this.identifiant = new SimpleIntegerProperty(identifiant);
        this.nbPlayers = new SimpleIntegerProperty(nbPlayers);
        this.dimensions = new SimpleStringProperty("?x?");
    }

    public Partie(int identifiant, int nbPlayers, boolean creator) {
        this.identifiant = new SimpleIntegerProperty(identifiant);
        this.nbPlayers = new SimpleIntegerProperty(nbPlayers);
        this.dimensions = new SimpleStringProperty("?x?");
        this.creator = creator;
    }

    public int getIdentifiant() {
        return identifiant.get();
    }

    public IntegerProperty identifiantProperty() {
        return identifiant;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant.set(identifiant);
    }

    public int getNbPlayers() {
        return nbPlayers.get();
    }

    public IntegerProperty nbPlayersProperty() {
        return nbPlayers;
    }

    public String getDimensions() {
        return dimensions.get();
    }

    public StringProperty dimensionsProperty() {
        return dimensions;
    }

    public void setNbPlayers(int nbPlayers) {
        this.nbPlayers.set(nbPlayers);
    }

    public void setDimensions(int h, int w){
        this.dimensions = new SimpleStringProperty(h + "x" + w);
    }

    public ObservableList<String> getPlayersNames() {
        return playersNames;
    }

    public void setPlayersNames(ObservableList<String> playersNames) {
        this.playersNames = playersNames;
    }

    public int getNbGhosts() {
        return nbGhosts.get();
    }

    public IntegerProperty nbGhostsProperty() {
        return nbGhosts;
    }

    public void setNbGhosts(int nbGhosts) {
        this.nbGhosts.set(nbGhosts);
    }

    public boolean isCreator() {
        return creator;
    }

    public void setCreator(boolean creator) {
        this.creator = creator;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getDimensionX(){
        return Integer.parseInt(this.dimensions.get().split("x")[0]);
    }

    public int getDimensionY(){
        return Integer.parseInt(this.dimensions.get().split("x")[1]);
    }
}
