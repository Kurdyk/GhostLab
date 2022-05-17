package Utils.Request;

public class REGIS extends Request {

    private String id;
    private String port;
    private byte m;

    public REGIS(String id, String port, byte m) {
        this.id = id;
        this.port = port;
        this.m = m;
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

    public byte getM() {
        return m;
    }
}
