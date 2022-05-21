package utils;

import models.Config;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Connection handler.
 */
public class ConnectionHandler extends Thread{
    private Config config;
    private boolean running = true;
    private RepParser scanner;
    private TestWriter writer;
    private Socket socket;
    private MessagerieMulticast messagerieMulticast;
    private MessageriePrivee messageriePrivee;
    private ConcurrentHashMap<String, CallbackServer> callLinks = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, CallbackInstance> callOwners = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<String>> buffers = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Connection handler.
     *
     * @param config the config
     */
    public ConnectionHandler(Config config) {
        this.config = config;
    }

    public int getSocketPort() {
        return this.socket.getPort();
    }

    /**
     * Quitter.
     */
    public void quitter(){
        writer.send("QUITS").end();
        running = false;
        if (socket != null){
            try {
                socket.close();
            } catch (IOException ignored){}
        }
    }


    public TestWriter getWriter() {
        return this.writer;
    }

    /**
     * Register callback.
     *
     * @param code       the code
     * @param controller the controller
     * @param callback   the callback
     */
    public void registerCallback(String code, CallbackInstance controller, CallbackServer callback){
        callLinks.put(code, callback);
        callOwners.put(code, controller);
        if(buffers.get(code) != null){
            buffers.get(code).clear();
        }
    }

    /**
     * Register callback.
     *
     * @param code           the code
     * @param controller     the controller
     * @param callback       the callback
     * @param processBuffers the process buffers
     */
    public synchronized void registerCallback(String code, CallbackInstance controller, CallbackServer callback, boolean processBuffers) {
        if (processBuffers && buffers.get(code) != null) {
            for (String command : buffers.get(code)) {
                callback.call(controller, command);
            }
        }
        registerCallback(code, controller, callback);
    }

    /**
     * Release callback.
     *
     * @param code the code
     */
    public void releaseCallback(String code){
        callLinks.remove(code);
        callOwners.remove(code);
    }

    /**
     * Register recurrent server call timer.
     *
     * @param recurrentServerRequest the recurrent server request
     * @param delay                  the delay
     * @return the timer
     */
    public Timer registerRecurrentServerCall(RecurrentServerRequest recurrentServerRequest, int delay){
        Timer timer = new Timer(true);
        recurrentServerRequest.setHandler(this);
        timer.schedule(recurrentServerRequest, 0, delay);
        return timer;
    }

    /**
     * Upgrade.
     */
    public void upgrade(){
        writer.send("UPGD?").end();
    }

    public void clearAll(){
        callLinks.clear();
        callOwners.clear();
    }

    public void clearAll(boolean clearbuffers){
        clearAll();
        if (clearbuffers) buffers.clear();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(config.getAdresseServeur(), config.getPortServeur());
            this.scanner = new RepParser(socket.getInputStream(), "***");
            writer = new TestWriter(socket.getOutputStream(), "***");
            messageriePrivee = new MessageriePrivee(this);
            new Thread(messageriePrivee).start();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Something went wrong...");
            return;
        }
        if (this.config.isServeurAmeliore()){
            upgrade();
        }
        while (running){
            String command = null;
            try {
                command = scanner.parse();
            } catch (IOException e) {
                System.out.println("Socket closed by host");
                System.exit(1);
            } catch (IllegalArgumentException e) {
                continue;
            }
            System.out.println("RECU : " + command);
            exec(command);
        }
    }

    public void exec(String command) {
        String[] response = command.split("\\u0020");
        if (callLinks.containsKey(response[0])) {
            callLinks.get(response[0]).call(callOwners.get(response[0]), command);
        } else {
            buffers.computeIfAbsent(response[0], k -> new ArrayList<>());
            buffers.get(response[0]).add(command);
        }
    }

    public void setMessagerieMulticast(MessagerieMulticast _messagerieMulticast) {
        this.messagerieMulticast = _messagerieMulticast;
        new Thread(messagerieMulticast).start();
    }

    public MessageriePrivee getMessageriePrivee() {
        return this.messageriePrivee;
    }
}
