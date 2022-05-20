package Utils;

import Apps.ConnectionHandler;
import Models.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Client handler.
 */
public class ClientHandler implements Runnable {

    private ConnectionHandler mainApp;
    private Socket socket;
    private TestWriter writer;
    private Client client;
    private String username;
    private RequestParser parser;
    private boolean loggedIn;
    private String start_buffer = null;



    private final CopyOnWriteArrayList<Integer> sendingQueue = new CopyOnWriteArrayList<>();

    /**
     * Instantiates a new Client handler.
     *
     * @param socket  the socket
     * @param mainApp the main app
     * @throws Exception the exception
     */
    public ClientHandler(Socket socket, ConnectionHandler mainApp) throws Exception{
        this.socket = socket;
        this.mainApp = mainApp;
        this.writer = new TestWriter(socket.getOutputStream(), "***");
        this.parser = new RequestParser(socket.getInputStream(), this, mainApp);
        this.client = new Client();

    }

    /**
     * Instantiates a new Client handler.
     */
    public ClientHandler(){

    }

    /**
     * send to the client socket the server's game's list
     */
    @Override
    public void run(){
        try {
            System.out.println("Connected from " + socket);
            int nb_parties = mainApp.getAvailableGamesNumber();
            writer.send("GAMES ").send((byte) nb_parties).end();
            mainApp.getAvailableGamesMap().forEach((id, game)
                    -> writer.send("OGAME ")
                            .send((byte) id.intValue())
                            .send(" ")
                            .send((byte) game.getNb_players())
                            .end());

        } catch (Exception e){
            e.printStackTrace();
        }

        boolean running = true;
        while(running) {
            try {
                parser.parse();
            } catch (IOException e) {
                System.out.println("ON EST BIEN DANS LE CATCH");
                //System.out.println("Socket closed by client");
                try {
                    socket.close();
                } catch (IOException ignored) {}
                running = false;
            } catch (SocketClosedException e){
                System.out.println("ON A REMARQUE QUE LA SOCKET ETAIT FERMEE BORDEL DE MERDE");
                running = false;
            }
        }
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * New client.
     */
    public void newClient(){
        this.client = new Client();
    }


    /**
     * Close connection.
     */
    protected void closeConnection(){
        try{
            socket.close();
            if (username != null) {
                mainApp.usernamesSet.remove(username);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Is logged in boolean.
     *
     * @return the boolean
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets logged in.
     *
     * @param loggedIn the logged in
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }


    /**
     * Is good client boolean.
     *
     * @return the boolean
     */
    public boolean isGoodClient(){
        return parser.isGoodClient();
    }

    /**
     * get ipaddress links to the socket
     * @return InetAddress links to socket
     */
    public InetAddress getIp() {
        return this.socket.getInetAddress();
    }

    /**
     * get writer
     * @return a TestWriter
     */
    public TestWriter getWriter() {
        return writer;
    }
}
