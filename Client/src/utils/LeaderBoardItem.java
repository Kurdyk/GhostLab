package utils;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Callback;

/**
 * The type Leader board item.
 */
public class LeaderBoardItem implements Comparable<LeaderBoardItem> {
    private final StringProperty username;
    private final StringProperty rank;
    private final IntegerProperty score;
    private final BooleanProperty alive;

    /**
     * Instantiates a new Leader board item.
     *
     * @param username the username
     * @param rank     the rank
     * @param score    the score
     */
    public LeaderBoardItem(String username, String rank, Integer score) {
        this.username = new SimpleStringProperty(username);
        this.rank = new SimpleStringProperty(rank);
        this.score = new SimpleIntegerProperty(score);
        this.alive = new SimpleBooleanProperty(true);
    }

    /**
     * Instantiates a new Leader board item.
     */
    public LeaderBoardItem(){
        this(null, null, null);
    }

    @Override
    public int compareTo(LeaderBoardItem itemToCompare) {
        if(!isAlive() && itemToCompare.isAlive()){
            return itemToCompare.getScore() - this.getScore() + 1000000;
        } else if(isAlive() && !itemToCompare.isAlive()){
            return itemToCompare.getScore() - this.getScore() - 1000000;
        }
        return itemToCompare.getScore() - this.getScore();
    }

    /**
     * Extractor callback.
     *
     * @return the callback
     */
    public static Callback<LeaderBoardItem, Observable[]> extractor() {
        return param -> new Observable[]{param.username, param.rank, param.score, param.alive};
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Username property string property.
     *
     * @return the string property
     */
    public StringProperty usernameProperty() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * Gets rank.
     *
     * @return the rank
     */
    public String getRank() {
        return rank.get();
    }

    /**
     * Rank property string property.
     *
     * @return the string property
     */
    public StringProperty rankProperty() {
        return rank;
    }

    /**
     * Sets rank.
     *
     * @param rank the rank
     */
    public void setRank(String rank) {
        this.rank.set(rank);
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Score property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(int score) {
        this.score.set(score);
    }

    /**
     * Is alive boolean.
     *
     * @return the boolean
     */
    public boolean isAlive() {
        return alive.get();
    }

    /**
     * Alive property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty aliveProperty() {
        return alive;
    }

    /**
     * Kill.
     */
    public void kill() {
        this.alive.set(false);
    }
}
