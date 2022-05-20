package Models.Games;

import Models.Entity;

public class Ghost extends Entity {
   private int values;

   public Ghost(int v, Coordinates c) {
       super(c);
       this.values = v;
   }

   public Ghost(Coordinates _coordinates) {
       super(_coordinates);
       this.values = 10;
   }

    public int getValues() {
        return values;
    }

    public void setValues(int values) {
        this.values = values;
    }


}
