package models.Game;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class CaseInconnue extends Case {

    public CaseInconnue(int X, int Y, ArrayList<Image> listeImages) {
        super(X, Y);
        this.isFree = true;
        this.imageCase = listeImages.get(1);

    }
}
