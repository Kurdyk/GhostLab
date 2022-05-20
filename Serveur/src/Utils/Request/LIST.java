package Utils.Request;

public class LIST extends Request {

    private byte m;

    /**
     * constructor
     * @param m
     */
    public LIST(byte m) {
        this.m = m;
    }

    /**
     * get m
     * @return m's value
     */
    public byte getM() {
        return m;
    }
}