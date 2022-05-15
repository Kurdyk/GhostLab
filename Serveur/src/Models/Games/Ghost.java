package Models.Games;

public class Ghost {
   private int values;
   private Coordinates coordinates;

    public Ghost(int v, Coordinates c){
        this.values = v;
        this.coordinates = c;
    }

    public Ghost(Coordinates c){
        this.values=50;
        this.coordinates = c;
    }

    public int getValues() {
        return values;
    }

    public void setValues(int values) {
        this.values = values;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

}
