import Apps.ConnectionHandler;
import Utils.ClientHandler;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Point d'entrée du serveur

/**
 * The type Tresor hunt server.
 */
public class TresorHuntServer {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        ConnectionHandler handler = new ConnectionHandler();
    }
}
