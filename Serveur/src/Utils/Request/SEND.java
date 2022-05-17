package Utils.Request;

public class SEND extends Request {

    private String id;
    private String message;

    public SEND(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
