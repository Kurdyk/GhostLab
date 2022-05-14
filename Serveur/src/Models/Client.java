package Models;

import Models.Games.Game;
import Utils.Coordinates;

/**
 * The type Client.
 */
public class Client {

    private Game gameRunning;
    private Coordinates coordonnees = new Coordinates(0,0);
    private boolean alive = true;
    private int score;
    private int port_udp;
    private String name;


    /**
     * Instantiates a new Client.
     */
    public Client(){}

    /**
     * Gets coordonnees.
     *
     * @return the coordonnees
     */

    public Coordinates getCoordonnees() {
        return coordonnees.copy();
    }

    /**
     * Sets coordonnees.
     *
     * @param coordonnees the coordonnees
     */

    public void setCoordonnees(Coordinates coordonnees) {
        this.coordonnees.setX(coordonnees.getX());
        this.coordonnees.setY(coordonnees.getY());
    }
    public void addToX(int x){
        this.coordonnees.addToX(x);
    }
    public void addToY(int y){
        this.coordonnees.addToY(y);
    }



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

    public int getPort_udp() {
        return port_udp;
    }

    public void setPort_udp(int port_udp) {
        this.port_udp = port_udp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
