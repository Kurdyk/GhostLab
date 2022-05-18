package utils;

/**
 * The type Coordinates.
 */
public class Coordinates {
    /**
     * The enum Orientation.
     */
    enum Orientation {
        /**
         * Nord orientation.
         */
        NORD,
        /**
         * Sud orientation.
         */
        SUD,
        /**
         * Est orientation.
         */
        EST,
        /**
         * Ouest orientation.
         */
        OUEST
        }
    private int x;
    private int y;
    private int value;

    private Orientation orientation;

    /**
     * Instantiates a new Coordinates.
     *
     * @param x the x
     * @param y the y
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;
    }

    /**
     * Instantiates a new Coordinates.
     *
     * @param x     the x
     * @param y     the y
     * @param value the value
     */
    public Coordinates(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Add to value.
     *
     * @param diff the diff
     */
    public void addToValue(int diff){
        this.value += diff;
    }


    public void setValue(int value){
        this.value = value;
    }

    /**
     * Decrease value.
     *
     * @param diff the diff
     */
    public void decreaseValue(int diff) { this.value -= diff; }


    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        if(x - this.x > 0){
            orientation = Orientation.EST;
        } else {
            orientation = Orientation.OUEST;
        }
        this.x = x;
    }


    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
        if (y - this.y > 0){
            orientation = Orientation.SUD;
        } else {
            orientation = Orientation.NORD;
        }
        this.y = y;
    }

    /**
     * Add to x.
     *
     * @param diff the diff
     */
    public void addToX(int diff){
        if (diff > 0){
            orientation = Orientation.EST;
        } else {
            orientation = Orientation.OUEST;
        }
        this.x += diff;
    }

    /**
     * Add to y.
     *
     * @param diff the diff
     */
    public void addToY(int diff){
        if (diff > 0) {
            orientation = Orientation.SUD;
        } else {
            orientation = Orientation.NORD;
        }
        this.y += diff;
    }

}
