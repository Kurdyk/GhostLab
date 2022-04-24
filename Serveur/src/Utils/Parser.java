package Utils;


import Apps.ConnectionHandler;
import Models.Games.Game;

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
        if (!CommandValidator.valiate(response_text)) illegalCommand();
        String[] response = response_text.split(" ");
        switch (response[0]){
            case "GAME?":
                client.send("GAMES " + (char) mainHandler.getAvailableGamesNumber());
                for (Game g: mainHandler.getAvailableGamesMap().values()){
                    client.send("OGAME " + (char) g.getId() + " " + g.getNb_players());
                }
                break;
            case "SIZE?":
                int i = response[1].charAt(0);
                Game g = mainHandler.getAvailableGamesMap().get(i);
                client.send("SIZE! " + (char) g.getId() + " " + (char) g.getDimX() + " " + (char) g.getDimY());
                break;
            case "LIST?":
                int id = response[1].charAt(0);
                Game ga = mainHandler.getAvailableGamesMap().get(id);
                client.send("LIST! " + (char) id + " " + (char) ga.getNb_players());
                for (ClientHandler player: ga.getPlayers()){
                    client.send("PLAYR " + player.getUsername());
                }
                break;
            default:
                illegalCommand();
                break;
        }
    }
}