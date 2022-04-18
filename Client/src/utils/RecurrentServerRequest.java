package utils;

import java.util.TimerTask;

/**
 * The type Recurrent server request.
 */
public abstract class RecurrentServerRequest extends TimerTask {
    /**
     * The Handler.
     */
    public ConnectionHandler handler;

    /**
     * Instantiates a new Recurrent server request.
     */
    public RecurrentServerRequest() {
    }

    /**
     * Sets handler.
     *
     * @param handler the handler
     */
    public void setHandler(ConnectionHandler handler) {
        this.handler = handler;
    }


    public void run(){}
}
