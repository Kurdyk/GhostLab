package Utils;

import Apps.ConnectionHandler;
import Models.Client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Client handler.
 */
public class ClientHandler implements Runnable {

    private ConnectionHandler mainApp;
    private Socket socket;
//    private MyBufferedReader scanner;
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



            while(true) {
                try {
                    parser.parse();
                } catch (IOException e) {
                    System.out.println("Socket closed by client");
                    socket.close();
                    return;
                }
            }

        } catch (Exception e){
            e.printStackTrace();
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

//    /**
//     * Send.
//     *
//     * @param message the message
//     */
//    public synchronized void send(String message){
//        System.out.println("SENDING " + message + "***");
//        this.myPrintWriter.println(message);
//    }


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

    public InetAddress getIp() {
        return this.socket.getInetAddress();
    }

    public TestWriter getWriter() {
        return writer;
    }
}
