package utils;

/**
 * The interface Callback server.
 */
public interface CallbackServer {
    /**
     * Call.
     *
     * @param controller the controller
     * @param message    the message
     */
    void call(CallbackInstance controller, String message);
}
