package Models.Cases;

import Models.Games.Ghost;
import Utils.ClientHandler;

/**
 * The type Case.
 */
public abstract class Case {

    /**
     * The Pos vert.
     */
    protected int posVert;
    /**
     * The Pos hor.
     */
    protected int posHor;
    /**
     * The Player on.
     */
    protected ClientHandler playerOn;

    /**
     * The ghost on
     */
    private Ghost ghostOn;

    /**
     * The Is marked.
     */
// Variable de type algorithmique
    protected boolean isMarked;
    /**
     * The Is marked for destruction.
     */
    protected boolean isMarkedForDestruction;

    /**
     * Is free boolean.
     *
     * @return the boolean
     */
    public boolean isFree() {
        return true;
    }

    /**
     * Free.
     */
    public void free(){
        playerOn = null;
    }

    /**
     * Is marked boolean.
     *
     * @return the boolean
     */
    public boolean isMarked() { return isMarked;}

    /**
     * Sets marked.
     *
     * @param value the value
     */
    public void setMarked(boolean value) { isMarked=value; }


    /**
     * Set player on.
     *
     * @param player the player
     */
    public void setPlayerOn(ClientHandler player){

    }

    /**
     * Set a ghost in the case
     * @param ghost the ghost to set
     */
    public void setGhostOn(Ghost ghost) {
        if (playerOn == null && ghostOn == null) {
            ghostOn = ghost;
        } else {
            throw new IllegalStateException();
        }
    }

    public void capturedGhost(){
        this.ghostOn = null;
    }

    /**
     * Getter for ghostOn
     * @return ghostOn
     */
    public Ghost getGhostOn() { return this.ghostOn; }

    /**
     * Get x int.
     *
     * @return the int
     */
    public int getX(){
        return posHor;
    }

    /**
     * Get y int.
     *
     * @return the int
     */
    public int getY(){
        return posVert;
    }

    /**
     * Instantiates a new Case.
     *
     * @param X the x
     * @param Y the y
     */
    public Case(int X, int Y) {
        this.posVert = Y;
        this.posHor = X;
    }

    /**
     * Instantiates a new Case.
     *
     * @param original the original
     */
    public Case(Case original){
        this.posVert = original.posVert;
        this.posHor = original.posHor;
        this.playerOn = original.playerOn;
        this.isMarked = original.isMarked;
        this.isMarkedForDestruction = original.isMarkedForDestruction;
    }

    /**
     * Copy case.
     *
     * @return the case
     */
    public Case copy(){
        return null;
    }

    /**
     * Is marked for destruction boolean.
     *
     * @return the boolean
     */
    public boolean isMarkedForDestruction() {
        return isMarkedForDestruction;
    }

    /**
     * Sets marked for destruction.
     *
     * @param markedForDestruction the marked for destruction
     */
    public void setMarkedForDestruction(boolean markedForDestruction) {
        isMarkedForDestruction = markedForDestruction;
    }


    /**
     * Gets player on.
     *
     * @return the player on
     */
    public ClientHandler getPlayerOn() {
        return playerOn;
    }
}