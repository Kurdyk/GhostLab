package Utils.Request;

public class SEND extends Request {

    private String id;
    private String message;

    /**
     * constructor
     * @param id
     * @param message
     */
    public SEND(String id, String message) {
        this.id = id;
        this.message = message;
    }

    /**
     * get id
     * @return id's value
     */
    public String getId() {
        return id;
    }

    /**
     * get message
     * @return message's value
     */
    public String getMessage() {
        return message;
    }
}
