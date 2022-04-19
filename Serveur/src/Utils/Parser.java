package Utils;


import Apps.ConnectionHandler;
import Models.Games.Game;

import java.util.ArrayList;

/**
 * This class is used to parse messages from clients,
 * create java objects of the command
 * and call methods to process them
 */
public class Parser {

    //Attributs

    private final ClientHandler client;
    private final ConnectionHandler mainHandler;
    private boolean goodClient = false;


    /**
     * Instantiates a new Parser.
     *
     * @param client  the client
     * @param handler the handler
     */
//Constructeur
    public Parser(ClientHandler client, ConnectionHandler handler) {
        this.client = client;
        this.mainHandler = handler;
    }




    //Méthodes
    private void illegalCommand(){
        client.send("FUCKU");
        for (Integer id : client.getJoinedGames()){
            //mainHandler.getAvailableGamesMap().get(id).removePlayer(client);
        }
        client.closeConnection();
        if (client.isLoggedIn()){
            mainHandler.usernamesSet.remove(client.getUsername());
        }
    }

    /**
     * Is good client boolean.
     *
     * @return the boolean
     */
    public boolean isGoodClient() {
        return goodClient;
    }

    /**
     * Sets good client.
     *
     * @param goodClient the good client
     */
    public void setGoodClient(boolean goodClient) {
        this.goodClient = goodClient;
    }

    /**
     * Parse.
     *
     * @param response_text the response text
     */
    //Méthode principale qui lit la commande envoyée par l'utilisateur et agit en fonction
    public void parse(String response_text){
        System.out.println("PROCESSING COMMAND : "+ response_text);
        String[] response = response_text.split(" ");
        switch (response[0]){


            default:
                illegalCommand();
                break;
        }
    }
}