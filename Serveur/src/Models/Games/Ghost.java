package Models.Games;

import Models.Entity;

public class Ghost extends Entity {
   private int values;

   public Ghost(int v, Coordinates c) {
       super(c);
       this.values = v;
   }

    /**
     * constructor
     * @param _coordinates
     */
   public Ghost(Coordinates _coordinates) {
       super(_coordinates);
       this.values = 10;
   }

    /**
     *
     * @return values
     */
    public int getValues() {
        return values;
    }

    /**
     * set values' value
     * @param values
     */
    public void setValues(int values) {
        this.values = values;
    }


}
