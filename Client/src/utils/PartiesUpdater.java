package utils;

import Apps.MainApp;

import java.util.ArrayList;

/**
 * The type Parties updater.
 */
public class PartiesUpdater extends CallbackInstance {
    /**
     * The Main app.
     */
    MainApp mainApp;
    /**
     * The Total parties.
     */
    int totalParties;
    /**
     * The Parties list brute.
     */
    ArrayList<String> partiesListBrute = new ArrayList<>();

    /**
     * Instantiates a new Parties updater.
     *
     * @param mainApp the main app
     */
    public PartiesUpdater(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void parse(String message){
        String[] commande = message.split("\\u0020");
        System.out.println("parse de PartiesUpdater = Message en "+commande.length + " morceaux");
        if (commande[0].equals("GAMES") && commande.length == 2){
            this.totalParties = commande[1].charAt(0);
            System.out.println("On va recevoir " + this.totalParties + " parties.");
            partiesListBrute.clear();
            if(totalParties == 0){
                mainApp.updateParties(partiesListBrute);
            }
        } else if (commande[0].equals("OGAME") && commande.length == 3){
            partiesListBrute.add(message);
            System.out.println("Une nouvelle partie recue.");
            if (partiesListBrute.size() == totalParties){
                mainApp.updateParties(partiesListBrute);
            }
        } else if (commande[0].equals("SIZE!") && commande.length == 4){
            int id = commande[1].charAt(0);
            int h = commande[2].charAt(0);
            int w = commande[3].charAt(0);
            System.out.println("Pour la partie " + id + " on a " + h + "x" + w);
            mainApp.getPartiesList().stream().filter(p -> p.getIdentifiant() == id).forEach(p -> p.setDimensions(h, w));
        } else if (commande[0].equals("LIST!") && commande.length == 3){
            int id = commande[1].charAt(0);
            int nb = commande[2].charAt(0);
            mainApp.getPartiesList().stream().filter(p -> p.getIdentifiant() == id).forEach(p -> p.setNbPlayers(nb));
        }
    }


}
