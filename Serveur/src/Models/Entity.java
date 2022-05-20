package Models;

import Models.Games.Coordinates;

public abstract class Entity {

    private Coordinates coordinates;

    public Entity() {
        this.coordinates = new Coordinates(0, 0);
    }

    public Entity(Coordinates _coordinates) {
        this.coordinates = _coordinates;
    }

    public void move(String direction, int n) throws Exception {
        System.out.println("On bouge vers " + direction + " de " + n);
        switch (direction) {
            case "UP":
                this.coordinates = new Coordinates(this.coordinates.getX(), this.coordinates.getY() - n);
                break;
            case "DOWN":
                this.coordinates = new Coordinates(this.coordinates.getX(), this.coordinates.getY() + n);
                break;
            case "LEFT":
                this.coordinates = new Coordinates(this.coordinates.getX() - n, this.coordinates.getY());
                break;
            case "RIGHT":
                this.coordinates = new Coordinates(this.coordinates.getX() + n, this.coordinates.getY());
                break;
            default:
                System.out.println("Unrecognized direction");
                throw new Exception("Unrecognized direction");
        }
    }

    /**
     * get entity's coordinates
     * @return entity's coordinates
     */
    public Coordinates getCoordonnees() {
        return this.coordinates;
    }

    /**
     * set entity's coordinates
     * @param coordinates to set
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
