package Models.Games;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y){
        this.x=x;
        this.y=y;
    }

    public Coordinates(Coordinates coordinates){
        this.x=coordinates.x;
        this.y= coordinates.y;
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

    public void addToX(int diff){
        this.x += diff;
    }

    public void addToY(int diff){
        this.y += diff;
    }

    public Coordinates generateCoordinates(int h, int w){
        int x = (int) (Math.random() * h);
        int y = (int) (Math.random() * w);
        return (x<=h && y<=w ? new Coordinates(x,y) : generateCoordinates(h,w));
    }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
}
