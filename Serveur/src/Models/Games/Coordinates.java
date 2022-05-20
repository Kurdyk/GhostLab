package Models.Games;

public class Coordinates {
    private int x;
    private int y;

    /**
     * Coordinates' constructor
     * @param x
     * @param y
     */
    public Coordinates(int x, int y){
        this.x=x;
        this.y=y;
    }

    /**
     * Coordinates's constructor
     * @param coordinates
     */
    public Coordinates(Coordinates coordinates){
        this.x=coordinates.x;
        this.y= coordinates.y;
    }

    /**
     *
     * @return this' copy
     */
    public Coordinates copy(){return new  Coordinates(this);}

    /**
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * set x
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * set y
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * add to x
     * @param diff
     */
    public void addToX(int diff){
        this.x += diff;
    }

    /**
     * add to y
     * @param diff
     */
    public void addToY(int diff){
        this.y += diff;
    }

    /**
     * generate random Coordinates between terminals
     * @param h
     * @param w
     * @return Coordinates
     */
    public Coordinates generateCoordinates(int h, int w){
        int x = (int) (Math.random() * h);
        int y = (int) (Math.random() * w);
        return (x<=h && y<=w ? new Coordinates(x,y) : generateCoordinates(h,w));
    }

    /**
     *
     * @return String
     */

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
}
