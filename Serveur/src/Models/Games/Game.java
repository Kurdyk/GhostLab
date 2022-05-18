package Models.Games;

import Apps.ConnectionHandler;
import Models.Plateau;
import Utils.ClientHandler;

import java.util.ArrayList;
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

    private Plateau plateau;

    private ArrayList<Ghost> ghosts;

    ///Pour UDP et Multicast
    Messagerie messagerie;

    public Game(ClientHandler owner,  ConnectionHandler mainHandler) {

        this.owner= owner;
        this.mainHandler = mainHandler;
        this.nb_players = 0;
        this.nb_ready = 0;
        this.ghosts = new ArrayList<Ghost>();
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

    public ArrayList<Ghost> getGhosts() { return this.ghosts; }

    public Plateau getPlateau(){ return this.plateau;}

    public Messagerie getMessagerie(){return this.messagerie;}

    private void sendAll(String message){
        for (ClientHandler player: this.players){
            player.getWriter().send(message).end();
        }
    }

    private void sendGood(String message){
        for (ClientHandler player: this.players){
            if (player.isGoodClient()){
                player.getWriter().send(message).end();
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
            client.getWriter().send("REGNO").end();
            return;
        }
        for (ClientHandler c : players) {
            if (c.equals(client)) {
                client.getWriter().send("REGNO").end();
                return;
            }
        }
        sendGood("PLJND " + client.getClient().getName());
        players.add(client);
        nb_players++;
        client.getClient().setGameRunning(this);
        client.getWriter().send("REGOK ").send((byte) this.getId()).end();
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
            client.getWriter().send("DUNNO").end();
            return;
        }
        if (client.getClient().isReady()) {
            client.getClient().setReady(false);
            nb_ready--;
        }
        nb_players--;
        client.getWriter().send("UNROK ").send((byte) this.getId()).end();
        this.players.remove(client);
        this.sendGood("PQUIT " + client.getClient().getName());

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
            sendGood("PSTAT " + client.getClient().getName());
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
        this.nb_fantoms = (Math.max(10, 3 * nb_ready));
        this.plateau = new Plateau(dimX, dimY, nb_fantoms, dimX * dimY / 3, this);

        try {
            this.messagerie = new Messagerie(this.getId(), players);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ClientHandler client : players) {
            client.getWriter()
                    .send("WELCO ")
                    .send((byte) this.getId())
                    .send(" ")
                    .send((short) this.getDimY())
                    .send(" ")
                    .send((short) this.getDimX())
                    .send(" ")
                    .send((byte) this.nb_fantoms)
                    .send(" ")
                    .send(this.messagerie.getIp())
                    .send(" ")
                    .send(this.messagerie.getMulticastPort())
                    .end();
        }
    }

    public int ghostValue(int x, int y) throws Exception{
        for (Ghost g: ghosts) {
            if(Objects.equals(g.getCoordinates(), new Coordinates(x, y))){
                return g.getValues();
            }
        }
        return 0;
    }

    public void removeGhost(int x, int y) throws Exception{
        for (Ghost g: ghosts) {
            if(Objects.equals(g.getCoordinates(), new Coordinates(x, y))){
                ghosts.remove(g);
            }
        }
    }


}
