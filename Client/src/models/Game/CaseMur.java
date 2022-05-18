package models.Game;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * The type Case mur.
 */
public class CaseMur extends Case{

    private int durabilite = 100;

    /**
     * Instantiates a new Case mur.
     *
     * @param X           the x
     * @param Y           the y
     * @param listeImages the liste images
     */
    public CaseMur(int X, int Y, ArrayList<Image> listeImages) {
        super(X, Y);
        isFree = false;
        this.imageCase = listeImages.get(0);
    }

    @Override
    public void free() {}


    /**
     * Damage int.
     *
     * @param damages the damages
     * @return the int
     */
    public int damage(int damages){
        durabilite -= damages;
        return Math.max(durabilite, 0);
    }
}
