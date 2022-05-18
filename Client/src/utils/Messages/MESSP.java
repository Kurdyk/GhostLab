package utils.Messages;

public class MESSP {

    private String id;
    private String message;

    public MESSP(String id, String message) {
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
