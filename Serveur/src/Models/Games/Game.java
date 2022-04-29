package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    private final ConnectionHandler mainHandler;
    private final ClientHandler owner;
    private int nb_players;
    private final int id;
    private final int dimX;
    private final int dimY;
    private final CopyOnWriteArrayList<ClientHandler> players = new CopyOnWriteArrayList<>();
    private int maxPlayers;

    public Game(ClientHandler owner,  ConnectionHandler mainHandler) {

        this.owner= owner;
        this.mainHandler = mainHandler;
        this.nb_players = 0;
        this.id = mainHandler.registerGameId(this);
        dimX = generate_dim();
        dimY = generate_dim();
        maxPlayers = dimX * dimY / 5;

    }

    public static int generate_dim(){
        int d = (int) (Math.random() * 80) + 10;
        return (d == 42 ? generate_dim() : d);
    }


    public CopyOnWriteArrayList<ClientHandler> getPlayers() {
        return players;
    }

    public int getNb_players() { return this.nb_players; }

    public int getId() { return this.id; }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    private void sendAll(String message){
        for (ClientHandler player: this.players){
            player.send(message);
        }
    }

    private void sendGood(String message){
        for (ClientHandler player: this.players){
            if (player.isGoodClient()){
                player.send(message);
            }
        }
    }

    private boolean registerUsername(String username){
        return this.players.stream().noneMatch(p -> Objects.equals(p.getClient().getName(), username));
    }

    public void addPlayer(ClientHandler client){
        if (!registerUsername(client.getClient().getName()) && this.players.size() < this.maxPlayers) {
            client.send("REGNO");
            return;
        }
        sendGood("PLJND " + client.getUsername());
        players.add(client);
        if(players.size() == this.maxPlayers){
            mainHandler.hideGame(this.id);
        }
    }

}
