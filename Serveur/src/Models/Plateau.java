package Models;

//import java.util.ArrayList;
import Models.Cases.*;
import Models.Games.Coordinates;
import Models.Games.Game;
import Models.Games.Ghost;
import Utils.ClientHandler;
import Utils.Tracker;

import java.util.*;

import static java.lang.Math.*;

/**
 * The type Plateau.
 */
public class Plateau {

    // Variables liées aux paramètres de la grille de jeu
    private int vert;
    private int hor;
    private int nbMurs;
    private int nbGhostInit;
    private int nbGhost;

    private Case [][] grille;
    private ArrayList<Coordinates> coordinatesMurs = new ArrayList<>();
    private Game game;


    /**
     * Instantiates a new Plateau.
     *
     * @param hor       the hor
     * @param vert      the vert
     * @param nbMurs    the nb murs
     * @param game      the game
     */
    public Plateau(int hor, int vert, int nbGhostInit, int nbMurs, Game game) {
        this.vert=vert;
        this.hor=hor;
        this.nbGhostInit = nbGhostInit;
        this.nbGhost = 0;
        this.nbMurs=nbMurs;
        grille = new Case[hor][vert];
        this.game=game;

        System.out.println("GENERATION DU PLATEAU");


        coordinatesMurs.clear();
        generate();

        do {
            parcours();
        } while (!parcours());

        for(ClientHandler client : this.game.getPlayers()) {
            placerJoueurs(client);
        }

        placeGhost();

        System.out.println("PLATEAU GENERE");
    }

    /**
     * Hors limite boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean horsLimite (int x, int y) {
        return x < 0 || x >= hor || y < 0 || y >= vert;
    }



    private void reset() {
        for (int i=0; i<hor;i++) {
            for (int j=0; j<vert;j++) {
                grille[i][j].setMarked(false);
            }
        }
    }

    // Génère une grille en plaçant des éléments aléatoirement
    private void generate () {
        int tmpvert;
        int tmphor;
        int k=0;
        for (int i=0; i<hor; i++) {
            for (int j=0; j<vert; j++) {
                    grille[i][j] = new CaseVide(i,j);
            }
        }

        for (int i=0; i<nbMurs;i++) {
            do {
                tmpvert = (int)(Math.random() * vert);
                tmphor = (int)(Math.random() * hor);
            } while (grille[tmphor][tmpvert] instanceof CaseMur
                    ||!(grille[tmphor][tmpvert] instanceof CaseVide));
            grille[tmphor][tmpvert]=new CaseMur(tmphor,tmpvert);
            coordinatesMurs.add(new Coordinates(tmphor, tmpvert));
        }
    }

    private boolean connexe(int mI, int mJ) {
        for (int i=0;i<hor;i++) {
            for (int j=0;j<vert;j++) {
                if (!(grille[i][j].isMarked()) && (grille[i][j] instanceof CaseVide) ) {
                    System.out.println(i+" "+j+" pas connexe");
                    if (!chercheConnexe(i,j)) {
                        smartSuppr(i, j, mI, mJ);
                    }
                    return false;

                }
            }
        }
        return true;
    }



    private boolean parcours() {
        reset();
        int tmpvert;
        int tmphor;
        do {
            tmpvert = (int)(Math.random() * vert);
            tmphor = (int)(Math.random() * hor);
        } while (!(grille[tmphor][tmpvert] instanceof CaseVide));
        System.out.println("on teste i="+tmphor+ " j="+tmpvert);
        explore(tmphor, tmpvert);
        return connexe(tmphor,tmpvert);


    }

    private void supprime(int initI, int initJ, int finI, int finJ) {
        int i=initI;
        int j=initJ;
        if(finJ>initJ) {

            while (j<finJ) {

                System.out.println("supprime "+i+" "+j);

                supprime(i, j);

                j++;
            }
        }
        if(finJ<initJ) {

            while (j>finJ) {

                System.out.println("supprime "+i+" "+j);

                supprime(i, j);


                j--;
            }
        }
        if(finI>initI) {

            while (i<finI) {
                System.out.println("supprime "+i+" "+j);

                supprime(i, j);

                i++;
            }
        }
        if(finI<initI) {

            while (i>finI) {
                System.out.println("supprime "+i+" "+j);
                supprime(i, j);

                i--;
            }
        }
    }

    private boolean chercheConnexe(int i, int j) {

        int h = j;
        int b = j;
        int d = i;
        int g = i;
        while (d < hor || b < vert || g >= 0 || h >= 0) {
            d++;
            b++;
            g--;
            h--;
            if (!horsLimite(d,j) && grille[d][j].isMarked() && (grille[d][j] instanceof CaseVide)) {
                supprime(i, j, d, j);
                return true;
            }
            if (!horsLimite(g,j) &&  grille[g][j].isMarked() && (grille[g][j] instanceof CaseVide)) {
                supprime(i, j, g, j);
                return true ;
            }
            if (!horsLimite(i,h) && grille[i][h].isMarked() && (grille[i][h] instanceof CaseVide)) {

                supprime(i, j, i, h);
                return true;
            }
            if (!horsLimite(i,b) && grille[i][b].isMarked() && (grille[i][b] instanceof CaseVide)) {

                supprime(i, j, i, b);
                return true;
            }

        }
        return false;


    }




    private void supprime(int i, int j) {
        if (grille[i][j] instanceof CaseMur) {
            coordinatesMurs.removeIf(coordinates -> coordinates.getX() == i && coordinates.getY() == j);
            grille[i][j]=new CaseVide(i,j);
        }

    }

    private void smartSuppr(int initI, int initJ, int finI, int finJ) {
        int i=initI;
        int j=initJ;
        if (initI<=finI && initJ<=finJ) {
            while ( i<finI || j<finJ) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                    return;
                }
                if (i!=finI && j!=finJ) {
                    i++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                }
                if (i==finI) {
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i++;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                            return;
                        }
                    }
                }
            }
            return;
        }
        if (initI>=finI && initJ>=finJ) {
            while ((i>finI || j>finJ)) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {

                    return;
                }
                if (i!=finI && j!=finJ) {

                    i--;
                    supprime(i, j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                    j--;
                    supprime(i, j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                }
                if (i==finI) {
                    j--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i--;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                            return;
                        }
                    }
                }

            }
            return;
        }
        if (initI<=finI && initJ>=finJ) {
            while ( (i<finI || j>finJ)) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {

                    return;
                }
                if (i!=finI && j!=finJ) {
                    i++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                    j--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                }
                if (i==finI) {
                    j--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i++;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                            return;
                        }
                    }
                }

            }
            return;
        }
        if (initI>=finI && initJ<=finJ) {
            while ((i>finI || j<finJ)) {
                if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {

                    return;
                }
                if (i!=finI && j!=finJ) {
                    i--;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                }
                if (i==finI) {
                    j++;
                    supprime(i,j);
                    if (grille[i][j].isMarked() && (grille[i][j] instanceof CaseVide)) {
                        return;
                    }
                } else {
                    if (j==finJ) {
                        i--;
                        supprime(i,j);
                        if (grille[i][j].isMarked() && ( grille[i][j] instanceof CaseVide)) {
                            return;
                        }
                    }
                }


            }

        }
    }

    private void suppr (int initI, int initJ, int finI, int finJ) {
        int i=initI;
        int j=initJ;
        if (finI < initI) {
            while (!horsLimite(i,initJ) && i>=finI) {
                supprime(i, initJ);
                i--;
            }

        }
        if (finI > initI) {
            while (i<=finI) {
                supprime(i,initJ);
                i++;
            }

        }
        if (finJ < initJ) {
            while ( j>=finJ) {
                supprime(finI, j);
                j--;
            }
        }
        if (finJ > initJ) {
            while (j<=finJ) {
                supprime(finI,j);
                j++;
            }
        }

    }

    private void explore(int i, int j) {
        grille[i][j].setMarked(true);
        if (grille[i][j] instanceof CaseMur) return;

        if (!horsLimite(i,j+1) && !grille[i][j+1].isMarked()) explore(i,j+1);
        if (!horsLimite(i,j-1) && !grille[i][j-1].isMarked()) explore(i,j-1);
        if (!horsLimite(i+1,j) && !grille[i+1][j].isMarked()) explore(i+1,j);
        if (!horsLimite(i-1,j) && !grille[i-1][j].isMarked()) explore(i-1,j);
    }

    private void resetMarked(){
        for (int i=0; i<hor; i++) {
            for (int j=0; j<vert;j++) {
                grille[i][j].setMarked(false);
                grille[i][j].setMarkedForDestruction(false);
            }
        }
    }

    /**
     * Get case case.
     *
     * @param x the x
     * @param y the y
     * @return the case
     */
    public Case getCase(int x, int y){
        if (horsLimite(x, y)){
            return null;
        }
        return grille[x][y];
    }

    /**
     * Set case.
     *
     * @param c the c
     */
    public void setCase(Case c){
        grille[c.getX()][c.getY()] = c;
    }

    private void destroyCloseWall(int i, int j, Tracker tracker){
        grille[i][j].setMarkedForDestruction(true);
        if (grille[i][j] instanceof CaseMur && !tracker.getStatus()){
            System.out.println("ON DETRUIT LE MUR EN POSITION "+i+":"+j);
            grille[i][j] = new CaseVide(i,j);
            coordinatesMurs.removeIf(coordinates -> coordinates.getX() == i && coordinates.getY() == j);
            tracker.setStatus(true);
            return;
        }
        ArrayList<Integer> etapes = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Collections.shuffle(etapes);
        for (int e: etapes){
            switch (e){
                case 1:
                    if (!horsLimite(i,j+1) && !tracker.getStatus() && !grille[i][j+1].isMarkedForDestruction()) destroyCloseWall(i,j+1, tracker);
                    break;
                case 2:
                    if (!horsLimite(i,j-1) && !tracker.getStatus() && !grille[i][j-1].isMarkedForDestruction()) destroyCloseWall(i,j-1, tracker);
                    break;
                case 3:
                    if (!horsLimite(i+1,j) && !tracker.getStatus() && !grille[i+1][j].isMarkedForDestruction()) destroyCloseWall(i+1,j, tracker);
                    break;
                case 4:
                    if (!horsLimite(i-1,j) && !tracker.getStatus() && !grille[i-1][j].isMarkedForDestruction()) destroyCloseWall(i-1,j, tracker);
                    break;
            }
        }

    }

    private boolean estConnexe(int tmphor, int tmpvert) {
        int destroyed = 0;
        for (int i=0; i<hor; i++) {
            for (int j=0; j<vert;j++) {
                if (!(grille[i][j].isMarked()) && (grille[i][j] instanceof CaseVide)) {
                    System.out.println(i+", "+j+" n'est pas marquée");

                    destroyCloseWall(i, j ,new Tracker());
                    destroyed += 1;
                    resetMarked();
                    explorer(tmphor, tmpvert);
                    return estConnexe(tmphor, tmpvert);

                }
            }
        }
        System.out.println("Pour valider le plateau, on a du détruire "+destroyed+" murs");
        return true;
    }

    // On vérifie à partir de chaque position si la grille est connexe
    private boolean parcoursProfondeur() {

        int tmpvert = (int)(Math.random() * vert);
        int tmphor = (int)(Math.random() * hor);
        if (grille[tmphor][tmpvert] instanceof CaseVide){
            explorer(tmphor, tmpvert);
            return estConnexe(tmphor, tmpvert);
        } else {
            return parcoursProfondeur();
        }
    }

    private void explorer(int i, int j) {
        grille[i][j].setMarked(true);
        if (grille[i][j] instanceof CaseMur) return;
        if (!horsLimite(i,j+1) && !grille[i][j+1].isMarked()) explorer(i,j+1);
        if (!horsLimite(i,j-1) && !grille[i][j-1].isMarked()) explorer(i,j-1);
        if (!horsLimite(i+1,j) && !grille[i+1][j].isMarked()) explorer(i+1,j);
        if (!horsLimite(i-1,j) && !grille[i-1][j].isMarked()) explorer(i-1,j);
    }

    private void placerJoueurs(ClientHandler client) {
        int x = (int) (Math.random()*hor);
        int y = (int) (Math.random()*vert);
        if(this.grille[x][y] instanceof CaseVide && this.grille[x][y].getPlayerOn()==null) {
            this.grille[x][y].setPlayerOn(client);
            System.out.println("ON AFFECTE LES COORDONEES ("+x+", "+y+") AU CLIENT : "+client.getClient().getName());
            client.getClient().getCoordonnees().setX(x);
            client.getClient().getCoordonnees().setY(y);
            client.send("POSIT " + client.getClient().getName() + " " + fillCoordinate(x) + " " + fillCoordinate(y));
        } else {
            placerJoueurs(client);
        }
    }

    /**
     * Place les fantomes.
     */
    private void placeGhost() {

        ArrayList<Ghost> ghosts = this.game.getGhosts();

        while (nbGhostInit > nbGhost) {
            int x = (int) (Math.random()*hor);
            int y = (int) (Math.random()*vert);
            if(this.grille[x][y] instanceof CaseVide && this.grille[x][y].getPlayerOn() == null
                && this.grille[x][y].getGhostOn() == null) {
                Ghost toPlace = new Ghost(new Coordinates(x, y));
                this.grille[x][y].setGhostOn(toPlace);
                ghosts.add(toPlace);
                this.nbGhost++;
            } else {
                continue;
            }
        }
    }

    private String fillCoordinate(int n) {
        String res = "" + n;
        while (res.length() < 3) {
            res = "0" + res;
        }
        return res;
    }

    /**
     * Gets coordinates murs.
     *
     * @return the coordinates murs
     */
    public ArrayList<Coordinates> getCoordinatesMurs() {
        return coordinatesMurs;
    }

    ///TODO : WIP
    public int preshotMove(Client client, String direction, int n) throws Exception {
        int x = client.getCoordinates().getX();
        int y = client.getCoordinates().getY();
        int i;
        for (i = 1; i < n; i++) {
            switch (direction) {
                case "UP":
                    if (horsLimite(x, y - i) || !this.grille[x][y - i].isFree()) {
                        client.move(direction, i - 1);
                        collecte(client);
                        return i - 1;
                    }
                    break;
                case "DOWN":
                    if (horsLimite(x, y + i) || !this.grille[x][y + i].isFree()) {
                        client.move(direction, i - 1);
                        collecte(client);
                        return i - 1;
                    }
                    break;
                case "LEFT":
                    if (horsLimite(x - i, y ) || !this.grille[x - i][y].isFree()) {
                        client.move(direction, i - 1);
                        collecte(client);
                        return i - 1;
                    }
                    break;
                case "RIGHT":
                    if (horsLimite(x + i, y ) || !this.grille[x + i][y].isFree()) {
                        client.move(direction, i - 1);
                        collecte(client);
                        return i - 1;
                    }
                    break;
            }
        }

        return 0;
    }

    //TODO WIP
    public void collecte(Client client) throws Exception{
        int x = client.getCoordonnees().getX();
        int y = client.getCoordonnees().getY();
        if(this.grille[x][y].getGhostOn() != null){
            int score = this.game.ghostValue(x, y);
            client.addScore(score);
            this.game.getMessagerie().multicastMessage("SCORE "+client.getName()+" "+score+" "+x+" "+y);
            this.game.removeGhost(x,y);
            nbGhost --;
        }
    }

}