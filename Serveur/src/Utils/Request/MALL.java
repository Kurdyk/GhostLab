package Utils.Request;

public class MALL extends Request {

    private String message;

    /**
     * constructor
     * @param message
     */
    public MALL(String message) {
        this.message = message;
    }

    /**
     * get the message
     * @return message's value
     */
    public String getMessage() {
        return message;
    }
}
