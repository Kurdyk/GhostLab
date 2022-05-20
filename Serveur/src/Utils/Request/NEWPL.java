package Utils.Request;

public class NEWPL extends Request {

    private String id;
    private String port;

    /**
     * constructor
     * @param id
     * @param port
     */
    public NEWPL(String id, String port) {
        this.id = id;
        this.port = port;
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
     * @return port's integer's value
     */
    public int getPortValue() {
        return Integer.parseInt(port);
    }
}
