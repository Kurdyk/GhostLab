package Models.Games;

import Apps.ConnectionHandler;
import Utils.ClientHandler;
import Utils.MyPrintWriter;

import java.net.*;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    private final ConnectionHandler mainHandler;
    private final ClientHandler owner;
    private int nb_players;
    private int nb_ready;
    private int nb_fantoms;
    private final int id;
    private final int dimX;
    private final int dimY;
    private final CopyOnWriteArrayList<ClientHandler> players = new CopyOnWriteArrayList<>();
    private int maxPlayers;

    ///Pour UDP et Multicast
    Messagerie messagerie;

    public Game(ClientHandler owner,  ConnectionHandler mainHandler) {

        this.owner= owner;
        this.mainHandler = mainHandler;
        this.nb_players = 0;
        this.nb_ready = 0;
        this.id = mainHandler.registerGameId(this);
        dimX = generate_dim();
        dimY = generate_dim();
        maxPlayers = dimX * dimY / 5;
        this.nb_fantoms = 2 * maxPlayers;

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

    /**
     * Ajoute un joueur à la partie
     * @param client le joueur à ajouter
     */
    public synchronized void addPlayer(ClientHandler client){
        if (!registerUsername(client.getClient().getName()) && this.players.size() < this.maxPlayers) {
            client.send("REGNO");
            return;
        }
        sendGood("PLJND " + client.getUsername());
        for (ClientHandler c : players) {
            if (c.equals(client)) return;
        }
        players.add(client);
        nb_players++;
        client.send("REGOK " + (char) this.getId());
        if(players.size() == this.maxPlayers){
            mainHandler.hideGame(this.id);
        }
    }

    /**
     * Retire un joueur d'une partie
     * @param client
     */
    public synchronized void removePlayer(ClientHandler client) {
        if (!players.remove(client)) {
            client.send("DUNNO");
            return;
        }
        if (client.getClient().isReady()) {
            client.getClient().setReady(false);
            nb_ready--;
        }
        nb_players--;
        client.send("UNROK " + this.getId());

    }

    /**
     * ajoute un joueur qui se dit pret à la liste des joueurs prets et peut lancer la partie.
     * @param client
     */
    public synchronized void handleStart(ClientHandler client) {
        if (client.getClient().isReady());
        else {
            client.getClient().setReady(true);
            nb_ready++;
            if (nb_ready == nb_players) {
                startGame();
            }
        }
    }

    /**
     * Lance une partie
     */
    private void startGame() {
        this.mainHandler.getAvailableGamesMap().remove(this.getId());
        this.nb_fantoms = nb_ready * 3;

        try {
            this.messagerie = new Messagerie(this.getId(), players);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ClientHandler client : players) {
            client.send("WELCO " + (char) this.getId() + " "
                + MyPrintWriter.toLittleEndian((short) this.getDimY()) + " "
                    + MyPrintWriter.toLittleEndian((short) this.getDimX()) + " "
                        + (char) this.nb_fantoms + " "
                            + this.messagerie.getIp() + " "
                                + this.messagerie.getMulticastPort());
        }
    }



}
