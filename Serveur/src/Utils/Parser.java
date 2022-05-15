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
        try {
            this.client.getClient().getGameRunning().removePlayer(client);
        } catch (Exception e) {
            client.send("DUNNO");
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
        if (!CommandValidator.validate(response_text)) {
            illegalCommand();
            return;
        }
        String[] response = response_text.split("\\u0020");
        switch (response[0]){
            case "GAME?":
                client.send("GAMES " + (char) mainHandler.getAvailableGamesNumber());
                for (Game g: mainHandler.getAvailableGamesMap().values()){
                    client.send("OGAME " + (char) g.getId() + " " + (char) g.getNb_players());
                }
                break;
            case "SIZE?":
                int i = response[1].charAt(0);
                Game g = mainHandler.getAvailableGamesMap().get(i);
                if (g == null) {
                    client.send("DUNNO");
                    break;
                }
                client.send("SIZE! " + (char) g.getId() + " " + (char) g.getDimX() + " " + (char) g.getDimY());
                break;
            case "LIST?":
                int id = response[1].charAt(0);
                Game ga = mainHandler.getAvailableGamesMap().get(id);
                client.send("LIST! " + (char) id + " " + (char) (ga.getNb_players() == 42 ? 41 : ga.getNb_players() ));
                for (ClientHandler player: ga.getPlayers()){
                    client.send("PLAYR " + player.getClient().getName());
                }
                break;
            case "NEWPL":
                String pName = response[1];
                int pPort = Integer.parseInt(response[2]);
                this.client.newClient();
                this.client.getClient().setPort_udp(pPort);
                this.client.getClient().setName(pName);

                Game game = new Game(this.client, this.mainHandler);
                game.addPlayer(this.client);
                System.out.println(pName + " crée la partie : " + game.getId() + " avec le port : " + pPort);

                break;
            case "REGIS":
                pName = response[1];
                pPort = Integer.parseInt(response[2]);
                this.client.newClient();
                this.client.getClient().setPort_udp(pPort);
                this.client.getClient().setName(pName);
                int gameID = (int) response[3].charAt(0);

                System.out.println(pName + " s'inscrit dans la partie : " + gameID + " avec le port : " + pPort);

                try {
                    Game wantedGame = mainHandler.getAvailableGamesMap().get(gameID);
                    wantedGame.addPlayer(this.client);
                } catch (Exception e) {
                    client.send("REGNO");
                }
                break;
            case "UNREG":
                try {
                    this.client.getClient().getGameRunning().removePlayer(client);
                } catch (Exception e) {
                    client.send("DUNNO");
                }
                break;

            case "QUITS":
                client.closeConnection();
                if (client.isLoggedIn()){
                    mainHandler.usernamesSet.remove(client.getUsername());
                }
                break;

            case "START":
                try {
                    this.client.getClient().getGameRunning().handleStart(client);
                } catch (NullPointerException e) {
                    System.out.println("Not in a game");
                    illegalCommand();
                }
                break;

            default:
                System.out.println("Commande non reconnue");
                illegalCommand();
                break;
        }
    }
}