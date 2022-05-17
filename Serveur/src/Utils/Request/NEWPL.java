package Utils.Request;

public class NEWPL extends Request {

    private String id;
    private String port;

    public NEWPL(String id, String port) {
        this.id = id;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public String getPort() {
        return port;
    }

    public int getPortValue() {
        return Integer.parseInt(port);
    }
}
