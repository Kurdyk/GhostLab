package Apps;

import Models.Games.Game;
import Utils.AudioStreamingServer;
import Utils.ClientHandler;
import Utils.CommandValidator;

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

    private AudioStreamingServer streamingServer;
    private Thread streamingThread;


    /**
     * Instantiates a new Connection handler.
     *
     * @throws Exception the exception
     */
    public ConnectionHandler() throws Exception{
        CommandValidator.init();
        this.streamingServer = new AudioStreamingServer();
        this.streamingThread = new Thread(this.streamingServer);
        this.streamingThread.start();
        run();
    }

    /**
     * Register game id int.
     *
     * @param game the game
     * @return the int
     */
    public int registerGameId(Game game){
        int id = (int) Math.round(Math.random() * 126) + 1;
        gamesMap.put(id, game);
        availableGamesMap.put(id, game);
        return id;

    }

    /**
     * launch server socket
     * @throws Exception
     */
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

}
