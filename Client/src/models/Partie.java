package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The type Partie.
 */
public class Partie {

    private final IntegerProperty identifiant;
    private final IntegerProperty nbPlayers;

    private StringProperty dimensions;


    public Partie(int identifiant, int nbPlayers) {
        this.identifiant = new SimpleIntegerProperty(identifiant);
        this.nbPlayers = new SimpleIntegerProperty(nbPlayers);
        this.dimensions = new SimpleStringProperty("?x?");
    }

    public int getIdentifiant() {
        return identifiant.get();
    }

    public IntegerProperty identifiantProperty() {
        return identifiant;
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
}
