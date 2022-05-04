package Utils;

import Apps.ConnectionHandler;
import Models.Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The type Client handler.
 */
public class ClientHandler implements Runnable{

    private ConnectionHandler mainApp;
    private Socket socket;
    private Scanner scanner;
    private MyPrintWriter myPrintWriter;
    private Client client;
    private String username;
    private Parser parser;
    private boolean loggedIn;
    private final ArrayList<Integer> joinedGames = new ArrayList<>();
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
        this.scanner = new Scanner(socket.getInputStream());
        scanner.useDelimiter("\\s*\\*{3}\\s*");
        this.myPrintWriter = new MyPrintWriter(socket.getOutputStream(), true);
        this.parser = new Parser(this, mainApp);
        this.client = new Client();
        if (scanner.hasNext()){
            String c = scanner.next();
            if (c.equals("PING?")){
                myPrintWriter.println("PING!");
            } else {
                firstParse(c);
            }
        }
    }

    /**
     * Instantiates a new Client handler.
     */
    public ClientHandler(){

    }

    private void illegalCommand(){
        send("FUCKU");
        closeConnection();
        if (loggedIn){
            mainApp.usernamesSet.remove(username);
        }
    }

    private void firstParse(String command){
        System.out.println("PARSING " + command);
        switch (command){
            case "UPGD?":
                parser.setGoodClient(true);
                send("UPGD!");
                break;
            default:
                parser.parse(command);
                break;
        }
    }

    @Override
    public void run(){
        try {
            System.out.println("Connected from " + socket);
            int nb_parties = mainApp.getAvailableGamesNumber();
            myPrintWriter.println("GAMES " + (char) nb_parties);
            mainApp.getAvailableGamesMap().forEach((id, game)
                    -> myPrintWriter.println("OGAME " + (char) id.intValue() + " " + (char) (game.getNb_players())));

            while (scanner.hasNext()) {
                firstParse(scanner.next());
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

    /**
     * Send.
     *
     * @param message the message
     */
    public synchronized void send(String message){
        this.myPrintWriter.println(message);
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
     * Gets joined games.
     *
     * @return the joined games
     */
    public ArrayList<Integer> getJoinedGames() {
        return joinedGames;
    }

    /**
     * Is good client boolean.
     *
     * @return the boolean
     */
    public boolean isGoodClient(){
        return parser.isGoodClient();
    }

}
