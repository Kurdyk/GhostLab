package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

public class Game {

    private final ConnectionHandler mainHandler;
    private final ClientHandler owner;
    private int nb_players;
    private final int id;

    public Game(ClientHandler owner,  ConnectionHandler mainHandler) {

        this.owner= owner;
        this.mainHandler = mainHandler;
        this.nb_players = 0;
        this.id = mainHandler.registerGameId(this);

    }

    public int getNb_players() { return this.nb_players; }

    public int getId() { return this.id; }

}
