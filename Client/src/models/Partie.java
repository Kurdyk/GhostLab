package models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The type Partie.
 */
public class Partie {

    private IntegerProperty identifiant;
    private StringProperty createur;
    private StringProperty modeDeJeu;
    private IntegerProperty dimensionX;
    private IntegerProperty dimensionY;
    private IntegerProperty nombreDeTrous;
    private IntegerProperty nombreDeTresors;
    private IntegerProperty maxPlayers;
    private BooleanProperty robots;
    private boolean creator = false;
    private ObservableList<String> playersNames = FXCollections.observableArrayList();

    /**
     * Instantiates a new Partie.
     */
    public Partie(){
        this(-1, null, null, 0, 0, 0, 0, 0, false);
    }

    /**
     * Instantiates a new Partie.
     *
     * @param identifiant     the identifiant
     * @param createur        the createur
     * @param modeDeJeu       the mode de jeu
     * @param dimensionX      the dimension x
     * @param dimensionY      the dimension y
     * @param nombreDeTrous   the nombre de trous
     * @param nombreDeTresors the nombre de tresors
     * @param maxPlayers      the max players
     * @param robots          the robots
     */
    public Partie(int identifiant, String createur, String modeDeJeu, int dimensionX, int dimensionY, int nombreDeTrous, int nombreDeTresors, int maxPlayers, boolean robots) {
        this.identifiant = new SimpleIntegerProperty(identifiant);
        this.createur = new SimpleStringProperty(createur);
        this.modeDeJeu = new SimpleStringProperty(modeDeJeu);
        this.dimensionX = new SimpleIntegerProperty(dimensionX);
        this.dimensionY = new SimpleIntegerProperty(dimensionY);
        this.nombreDeTrous = new SimpleIntegerProperty(nombreDeTrous);
        this.nombreDeTresors = new SimpleIntegerProperty(nombreDeTresors);
        this.robots = new SimpleBooleanProperty(robots);
        this.maxPlayers = new SimpleIntegerProperty(maxPlayers);
    }

    /**
     * Gets identifiant.
     *
     * @return the identifiant
     */
    public int getIdentifiant() {
        return identifiant.get();
    }

    /**
     * Identifiant property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty identifiantProperty() {
        return identifiant;
    }

    /**
     * Sets identifiant.
     *
     * @param identifiant the identifiant
     */
    public void setIdentifiant(int identifiant) {
        this.identifiant.set(identifiant);
    }

    /**
     * Gets createur.
     *
     * @return the createur
     */
    public String getCreateur() {
        return createur.get();
    }

    /**
     * Createur property string property.
     *
     * @return the string property
     */
    public StringProperty createurProperty() {
        return createur;
    }

    /**
     * Sets createur.
     *
     * @param createur the createur
     */
    public void setCreateur(String createur) {
        this.createur.set(createur);
    }

    /**
     * Gets mode de jeu.
     *
     * @return the mode de jeu
     */
    public String getModeDeJeu() {
        return modeDeJeu.get();
    }

    /**
     * Mode de jeu property string property.
     *
     * @return the string property
     */
    public StringProperty modeDeJeuProperty() {
        return modeDeJeu;
    }

    /**
     * Sets mode de jeu.
     *
     * @param modeDeJeu the mode de jeu
     */
    public void setModeDeJeu(String modeDeJeu) {
        this.modeDeJeu.set(modeDeJeu);
    }

    /**
     * Gets dimension x.
     *
     * @return the dimension x
     */
    public int getDimensionX() {
        return dimensionX.get();
    }

    /**
     * Dimension x property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty dimensionXProperty() {
        return dimensionX;
    }

    /**
     * Sets dimension x.
     *
     * @param dimensionX the dimension x
     */
    public void setDimensionX(int dimensionX) {
        this.dimensionX.set(dimensionX);
    }

    /**
     * Gets dimension y.
     *
     * @return the dimension y
     */
    public int getDimensionY() {
        return dimensionY.get();
    }

    /**
     * Dimension y property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty dimensionYProperty() {
        return dimensionY;
    }

    /**
     * Sets dimension y.
     *
     * @param dimensionY the dimension y
     */
    public void setDimensionY(int dimensionY) {
        this.dimensionY.set(dimensionY);
    }

    /**
     * Gets nombre de trous.
     *
     * @return the nombre de trous
     */
    public int getNombreDeTrous() {
        return nombreDeTrous.get();
    }

    /**
     * Nombre de trous property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty nombreDeTrousProperty() {
        return nombreDeTrous;
    }

    /**
     * Sets nombre de trous.
     *
     * @param nombreDeTrous the nombre de trous
     */
    public void setNombreDeTrous(int nombreDeTrous) {
        this.nombreDeTrous.set(nombreDeTrous);
    }

    /**
     * Gets nombre de tresors.
     *
     * @return the nombre de tresors
     */
    public int getNombreDeTresors() {
        return nombreDeTresors.get();
    }

    /**
     * Nombre de tresors property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty nombreDeTresorsProperty() {
        return nombreDeTresors;
    }

    /**
     * Sets nombre de tresors.
     *
     * @param nombreDeTresors the nombre de tresors
     */
    public void setNombreDeTresors(int nombreDeTresors) {
        this.nombreDeTresors.set(nombreDeTresors);
    }

    /**
     * Is robots boolean.
     *
     * @return the boolean
     */
    public boolean isRobots() {
        return robots.get();
    }

    /**
     * Robots property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty robotsProperty() {
        return robots;
    }

    /**
     * Sets robots.
     *
     * @param robots the robots
     */
    public void setRobots(boolean robots) {
        this.robots.set(robots);
    }

    /**
     * Gets max players.
     *
     * @return the max players
     */
    public int getMaxPlayers() {
        return maxPlayers.get();
    }

    /**
     * Max players property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty maxPlayersProperty() {
        return maxPlayers;
    }

    /**
     * Sets max players.
     *
     * @param maxPlayers the max players
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers.set(maxPlayers);
    }

    /**
     * Is creator boolean.
     *
     * @return the boolean
     */
    public boolean isCreator() {
        return creator;
    }

    /**
     * Sets creator.
     *
     * @param creator the creator
     */
    public void setCreator(boolean creator) {
        this.creator = creator;
    }

    /**
     * Gets players names.
     *
     * @return the players names
     */
    public ObservableList<String> getPlayersNames() {
        return playersNames;
    }
}
