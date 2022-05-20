package Models;

import Models.Games.Coordinates;
import Models.Games.Game;

/**
 * The type Client.
 */
public class Client extends Entity {

    private Game gameRunning;
    private boolean alive = true;
    private int score;
    private int port_udp;
    private String name;
    private boolean ready = false;


    /**
     * Instantiates a new Client.
     */
    public Client(){
        super();
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

    public boolean isReady() { return ready;}

    public void setReady(boolean ready) {this.ready = ready;}

}
