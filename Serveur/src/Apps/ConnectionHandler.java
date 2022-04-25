package Apps;

import Models.Games.Game;
import Utils.ClientHandler;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Connection handler.
 */
public class ConnectionHandler {

    /**
     * The constant SERVER_VERSION.
     */
    public static double SERVER_VERSION = 1.0;
    /**
     * The Usernames set.
     */
    public CopyOnWriteArrayList<String> usernamesSet = new CopyOnWriteArrayList<>();
    private final Map<Integer, Game> gamesMap = new ConcurrentHashMap<Integer, Game>();
    private final Map<Integer, Game> availableGamesMap = new ConcurrentHashMap<Integer, Game>();


    /**
     * Instantiates a new Connection handler.
     *
     * @throws Exception the exception
     */
    public ConnectionHandler() throws Exception{
        run();
    }

    /**
     * Register game id int.
     *
     * @param game the game
     * @return the int
     */
    public int registerGameId(Game game){
        int id = (int) (Math.random() * (254) + 1);
        if (id == 42 || gamesMap.containsKey(id)){
            return registerGameId(game);
        } else {
            gamesMap.put(id, game);
            availableGamesMap.put(id, game);
            return id;
        }
    }

    private void run() throws Exception{
        ServerSocket listener = new ServerSocket(7236);
        System.out.println("Le serveur est actif sur le port " + listener.getLocalPort());
        ExecutorService pool = Executors.newFixedThreadPool(250);
        while (true) {
            pool.execute(new ClientHandler(listener.accept(), this));
        }
    }

    /**
     * Hide game boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean hideGame(int id){
        if (this.availableGamesMap.containsKey(id)){
            this.availableGamesMap.remove(id);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Show game boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean showGame(int id){
        if (this.availableGamesMap.containsKey(id)){
            return false;
        }
        else{
            this.availableGamesMap.put(id, this.gamesMap.get(id));
            return true;
        }
    }

    /**
     * Gets available games map.
     *
     * @return the available games map
     */
    public Map<Integer, Game> getAvailableGamesMap() {
        return availableGamesMap;
    }

    /**
     * Gets number of available games.
     *
     * @return the number of available games.
     */
    public int getAvailableGamesNumber() { return availableGamesMap.size(); }

    /**
     * Gets games map.
     *
     * @return the games map
     */
    public Map<Integer, Game> getGamesMap() {
        return gamesMap;
    }
}
