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

        // TODO: VERIFIER CE FONCTIONNEMENT
        //  @assign kurdyk
        switch (direction) {
            case "UP":
                this.coordinates = new Coordinates(this.coordinates.getX(), this.coordinates.getY() - n);
                break;
            case "DOWN":
                this.coordinates = new Coordinates(this.coordinates.getX(), this.coordinates.getY() + n);
                break;
            case "LEFT":
                this.coordinates = new Coordinates(this.coordinates.getX() + n, this.coordinates.getY());
                break;
            case "RIGHT":
                this.coordinates = new Coordinates(this.coordinates.getX() - n, this.coordinates.getY());
                break;
            default:
                throw new Exception("Unrecognized direction");
        }
    }

    public Coordinates getCoordonnees() {
        return this.coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
