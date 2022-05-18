package models;

import Apps.GameApp;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import models.Game.Case;
import models.Game.CaseInconnue;
import models.Game.CaseMur;
import models.Game.CaseVide;
import utils.CallbackInstance;
import utils.Coordinates;
import utils.LeaderBoardItem;

import java.util.*;

/**
 * The type Plateau.
 */
public class Plateau extends CallbackInstance {
    /**
     * The Plateau.
     */
    public ArrayList<ArrayList<Case>> plateau = new ArrayList<>();
    private int dimX;
    private int dimY;
    private int COEFF_IMAGE;
    private ArrayList<Image> listeImages;
    private HashMap<String, Coordinates> coordonneesJoueurs = new HashMap<>();
    private int compteToursRevealHole;
    private int trousRayon1;
    private int compteToursRevealMap;
    private int revealedX;
    private int revealedY;


    private GameApp gameApp;

    /**
     * Instantiates a new Plateau.
     *
     * @param dimX        the dim x
     * @param dimY        the dim y
     * @param COEFF_IMAGE the coeff image
     * @param gameApp     the game app
     */
    public Plateau(int dimX, int dimY, int COEFF_IMAGE, GameApp gameApp) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.COEFF_IMAGE = COEFF_IMAGE;
        this.gameApp = gameApp;

        listeImages = new ArrayList<>(Arrays.asList(new Image(Objects.requireNonNull(getClass().getResource("../Mur.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../inconnu.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../Vide.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_1_left.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_1_right.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_2_left.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false),
                new Image(Objects.requireNonNull(getClass().getResource("../player_2_right.png")).toExternalForm(), COEFF_IMAGE, COEFF_IMAGE, false, false)));




        for(int x = 0; x < dimX; x++){
            plateau.add(new ArrayList<Case>());
            for(int y = 0; y < dimY; y++){
                plateau.get(x).add(new CaseInconnue(x,y, listeImages));
            }
        }
    }

    /**
     * Gets plateau.
     *
     * @return the plateau
     */
    public ArrayList<ArrayList<Case>> getPlateau() {
        return plateau;
    }

    /**
     * Set case mur.
     *
     * @param x the x
     * @param y the y
     */
    public void setCaseMur(int x, int y){
        this.plateau.get(x).set(y, new CaseMur(x, y, listeImages));
    }



    @Override
    public void updatePlayerPosition(String s) {
        System.out.println("On a recu : "+s);
        String[] command = s.split(" ");
        String name = command[1];
        int x = Integer.parseInt(command[2]);
        int y = Integer.parseInt(command[3]);
        int p = Integer.parseInt(command[4]);
        Coordinates c = coordonneesJoueurs.get(name);
        if(c == null) {
            coordonneesJoueurs.put(name, new Coordinates(x, y, p));
            gameApp.getLeaderBoardItems().add(new LeaderBoardItem(name, "#1", 0));
            plateau.get(x).get(y).setVisitee();
        } else {
            c.setX(x);
            c.setY(y);
            c.setValue(p);
            LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> name.equals(i.getUsername())).findAny().orElse(null);
            assert item != null;
            if (item.getScore() != p) {
                Platform.runLater(() -> {
                    item.setScore(p);
                    trierLeaderBoard();
                });
            }
        }
    }
//
//    @Override
//    public void updatePlayerTresor(String s) {
//        coordonneesJoueurs.get(s.split(" ")[1]).addToValue(Integer.parseInt(s.split(" ")[6]));
//        LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> s.split(" ")[1].equals(i.getUsername())).findAny().orElse(null);
//        assert item != null;
//        Platform.runLater(() -> {
//                    item.setScore(item.getScore() + Integer.parseInt(s.split(" ")[6]));
//                    trierLeaderBoard();
//                });
//        updatePlayerPosition(s);
//        int x = coordonneesJoueurs.get(s.split(" ")[1]).getX();
//        int y = coordonneesJoueurs.get(s.split(" ")[1]).getY();
//        plateau.get(x).set(y, new CaseVide(x, y, listeImages));
//        gameApp.getConnectionHandler().send("512 "+s.split(" ")[1]+" UPDATED");
//    }


    @Override
    public void handleMove(String s){
        Coordinates c = this.coordonneesJoueurs.get(this.gameApp.getServerConfig().getUsername());
        int x = Integer.parseInt(s.split(" ")[1]);
        int y = Integer.parseInt(s.split(" ")[2]);

        if (x == c.getX() && y == c.getY()){
            handleMoveBlocked(s);
        } else {
            handleMoveAllowed(s);
        }
    }


    public void handleMoveAllowed(String s) {
        //TODO: JOUER UN SON DE MARCHE
        System.err.println("LA FONCTION HANDLEMOVEALLOWED A ETE APPELÉE AVEC LA CHAINE "+s+" ET LES DIRECTIONS : "+this.gameApp.getDirections());
        KeyCode code = this.gameApp.getDirections().get(0);
        gameApp.getDirections().remove(0);
        String username = gameApp.getServerConfig().getUsername();

        int x = Integer.parseInt(s.split(" ")[1]);
        int y = Integer.parseInt(s.split(" ")[2]);
        this.coordonneesJoueurs.get(username).setX(x);
        this.coordonneesJoueurs.get(username).setY(y);

        switch (code) {
            case UP, DOWN, LEFT, RIGHT -> this.plateau.get(x).set(y, new CaseVide(x, y, listeImages));
            default -> handleMoveAllowed(s);
        }

        this.plateau.get(x).set(y, new CaseVide(x, y, listeImages));
    }

    public void handleMoveBlocked(String s) {
        //TODO: JOUER UN SON POUR DIRE QU'ON A ETE BLOQUE
        KeyCode code = gameApp.getDirections().get(0);
        gameApp.getDirections().remove(0);

        int x = Integer.parseInt(s.split(" ")[1]);
        int y = Integer.parseInt(s.split(" ")[2]);
        switch(code){
            case UP -> this.plateau.get(x).set(y-1, new CaseMur(x, y-1, listeImages));
            case DOWN -> this.plateau.get(x).set(y+1, new CaseMur(x, y+1, listeImages));
            case LEFT -> this.plateau.get(x-1).set(y, new CaseMur(x-1, y, listeImages));
            case RIGHT -> this.plateau.get(x+1).set(y, new CaseMur(x+1, y, listeImages));
            default -> handleMoveBlocked(s);
        }
    }

    @Override
    public void handleMoveCapture(String s) {
        handleMoveAllowed(s);
        Coordinates coordinates = this.coordonneesJoueurs.get(this.gameApp.getServerConfig().getUsername());
        int p = Integer.parseInt(s.split(" ")[3]);
        coordinates.setValue(p);
        LeaderBoardItem item = gameApp.getLeaderBoardItems().stream().filter(i -> this.gameApp.getServerConfig().getUsername().equals(i.getUsername())).findAny().orElse(null);
        assert item != null;
        Platform.runLater(() -> {
                    item.setScore(p);
                    trierLeaderBoard();
                });
        this.plateau.get(coordinates.getX()).set(coordinates.getY(), new CaseVide(coordinates.getX(), coordinates.getY(), listeImages));
    }


    private void trierLeaderBoard(){
        Collections.sort(gameApp.getLeaderBoardItems());
        for (int i = 0; i < gameApp.getLeaderBoardItems().size(); i++){
            if (i != 0 && gameApp.getLeaderBoardItems().get(i-1).getScore() == gameApp.getLeaderBoardItems().get(i).getScore()){
                gameApp.getLeaderBoardItems().get(i).setRank(gameApp.getLeaderBoardItems().get(i-1).getRank());
            } else {
                gameApp.getLeaderBoardItems().get(i).setRank("#"+(i+1));
            }
        }
    }



    @Override
    public void partieFinie(String s) {
        String gagnant = s.split(" ")[1];
        gameApp.getTimer().stop();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("NOUS AVONS UN VAINQUEUR");
            alert.setContentText("Et le vainqueur est ... " +gagnant+"!");
            alert.initOwner(gameApp.getGameStage().getOwner());
            alert.setHeaderText(null);
            alert.showAndWait();

            gameApp.endGame();
        });
    }

    /**
     * Gets coeff image.
     *
     * @return the coeff image
     */
    public int getCOEFF_IMAGE() {
        return COEFF_IMAGE;
    }

    /**
     * Sets coeff image.
     *
     * @param COEFF_IMAGE the coeff image
     */
    public void setCOEFF_IMAGE(int COEFF_IMAGE) {
        this.COEFF_IMAGE = COEFF_IMAGE;
    }

    /**
     * Gets coordonnees joueurs.
     *
     * @return the coordonnees joueurs
     */
    public HashMap<String, Coordinates> getCoordonneesJoueurs() {
        return coordonneesJoueurs;
    }

    /**
     * Gets liste images.
     *
     * @return the liste images
     */
    public ArrayList<Image> getListeImages() {
        return listeImages;
    }


    /**
     * Sets trous rayon 1.
     *
     * @param trousRayon1 the trous rayon 1
     */
    public void setTrousRayon1(int trousRayon1) {
        this.trousRayon1=trousRayon1;
    }

    /**
     * Gets trous rayon 1.
     *
     * @return the trous rayon 1
     */
    public int getTrousRayon1() {
        return trousRayon1;
    }

    /**
     * Hors limite boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean horsLimite (int x, int y) {
        return x < 0 || x >= dimX || y < 0 || y >= dimY;
    }


    /**
     * Sets compte tours reveal hole.
     *
     * @param i the
     */
    public void setCompteToursRevealHole(int i) {
        compteToursRevealHole=i;
    }

    /**
     * Gets compte tours reveal hole.
     *
     * @return the compte tours reveal hole
     */
    public int getCompteToursRevealHole() {
        return compteToursRevealHole;
    }

    /**
     * Gets compte tours reveal map.
     *
     * @return the compte tours reveal map
     */
    public int getCompteToursRevealMap() { return compteToursRevealMap;}

    /**
     * Gets revealed x.
     *
     * @return the revealed x
     */
    public int getRevealedX() {return revealedX;}

    /**
     * Gets revealed y.
     *
     * @return the revealed y
     */
    public int getRevealedY() {return revealedY;}

    /**
     * Sets compte tours reveal map.
     *
     * @param compteToursRevealMap the compte tours reveal map
     */
    public void setCompteToursRevealMap(int compteToursRevealMap) {
        this.compteToursRevealMap = compteToursRevealMap;
    }

    /**
     * Gets dim x.
     *
     * @return the dim x
     */
    public int getDimX() {
        return dimX;
    }

    /**
     * Gets dim y.
     *
     * @return the dim y
     */
    public int getDimY() {
        return dimY;
    }
}
