package models.Game;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * The type Case vide.
 */
public class CaseVide extends Case {
    /**
     * Instantiates a new Case vide.
     *
     * @param X           the x
     * @param Y           the y
     * @param listeImages the liste images
     */
    public CaseVide(int X, int Y, ArrayList<Image> listeImages) {
        super(X, Y);
        this.isFree = true;
        this.imageCase = listeImages.get(2);
    }
}
