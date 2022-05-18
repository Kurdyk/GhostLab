package models.Game;


import javafx.scene.image.Image;

/**
 * The type Case.
 */
public abstract class Case {

    /**
     * The Is free.
     */
    protected boolean isFree;
    /**
     * The Pos vert.
     */
    protected int posVert;
    /**
     * The Pos hor.
     */
    protected int posHor;
    /**
     * The Image case.
     */
    protected Image imageCase;
    private int COEF_IMAGE;

    /**
     * The Visitee.
     */
// warfog
    protected boolean visitee;

    /**
     * Is free boolean.
     *
     * @return the boolean
     */
    public boolean isFree() {
        return isFree;
    }

    /**
     * Free.
     */
    public void free(){
        isFree = true;
    }

    /**
     * Gets image case.
     *
     * @return the image case
     */
    public Image getImageCase() {
        return imageCase;
    }

    /**
     * Get x int.
     *
     * @return the int
     */
    public int getX(){
        return posHor;
    }

    /**
     * Get y int.
     *
     * @return the int
     */
    public int getY(){
        return posVert;
    }

    /**
     * Instantiates a new Case.
     *
     * @param X the x
     * @param Y the y
     */
    public Case(int X, int Y) {
        this.posVert = Y;
        this.posHor = X;
        visitee=false;
    }

    /**
     * Sets visitee.
     */
    public void setVisitee() {
        visitee=true;
    }

    /**
     * Is visitee boolean.
     *
     * @return the boolean
     */
    public boolean isVisitee() { return visitee; }

}