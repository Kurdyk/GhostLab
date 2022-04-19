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
        String[] commande = message.split(" ");
        if (commande[1].equals("NUMBER")){
            this.totalParties = Integer.parseInt(commande[2]);
            partiesListBrute.clear();
            if(totalParties == 0){
                mainApp.updateParties(partiesListBrute);
            }
        } else if (commande[1].equals("MESS")){
            partiesListBrute.add(message);
            if (Integer.parseInt(commande[2]) == totalParties){
                mainApp.updateParties(partiesListBrute);
            }
        }
    }


}
