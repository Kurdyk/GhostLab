package Utils;

public class Coordinates {
    private int x;
    private int y;
    private int value;

    public Coordinates(int x, int y){
        this.x=x;
        this.y=y;
        this.value=0;
    }

    public Coordinates(int x, int y, int value){
        this.x=x;
        this.y=y;
        this.value=value;
    }
    public Coordinates(Coordinates coordinates){
        this.x=coordinates.x;
        this.y= coordinates.y;
        this.value=coordinates.value;
    }

    public Coordinates copy(){return new  Coordinates(this);}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addToX(int diff){
        this.x += diff;
    }

    public void addToY(int diff){
        this.y += diff;
    }
}
