package Utils.Request;

public class REGIS extends Request {

    private String id;
    private String port;
    private byte m;

    /**
     * constructor
     * @param id
     * @param port
     * @param m
     */
    public REGIS(String id, String port, byte m) {
        this.id = id;
        this.port = port;
        this.m = m;
    }

    /**
     * get id
     * @return id's value
     */
    public String getId() {
        return id;
    }

    /**
     * get port
     * @return port's value
     */
    public String getPort() {
        return port;
    }

    /**
     * get port's integer's value
     * @return port's integer value
     */
    public int getPortValue() {
        return Integer.parseInt(port);
    }

    /**
     * get m
     * @return m's value
     */
    public byte getM() {
        return m;
    }
}
