package Models;

import Models.Games.Game;
//import Utils.Coordinates;

import java.util.ArrayList;

/**
 * The type Client.
 */
public class Client {

    private Game gameRunning;
    //private Coordinates coordonnees = new Coordinates(0,0);
    private boolean alive = true;
    private int score;


    /**
     * Instantiates a new Client.
     */
    public Client(){}

    /**
     * Gets coordonnees.
     *
     * @return the coordonnees
     */
    /*
    public Coordinates getCoordonnees() {
        return coordonnees;
    }*/

    /**
     * Sets coordonnees.
     *
     * @param coordonnees the coordonnees
     */
    /*
    public void setCoordonnees(Coordinates coordonnees) {
        this.coordonnees = coordonnees;
    }
    */

    /**
     * Gets game running.
     *
     * @return the game running
     */
    public Game getGameRunning() {
        return gameRunning;
    }

    /**
     * Sets game running.
     *
     * @param gameRunning the game running
     */
    public void setGameRunning(Game gameRunning) {
        this.gameRunning = gameRunning;
    }

    /**
     * Is alive boolean.
     *
     * @return the boolean
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Kill.
     */
    public void kill() {
        this.alive = false;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() { return score; }

    /**
     * Add score.
     *
     * @param val the val
     */
    public void addScore(int val) {score+=val;}

    /**
     * Low score.
     *
     * @param val the val
     */
    public void lowScore(int val) {score-=val;}


}
