package Utils.Request;

public class SIZE extends Request {
     private byte m;

    /**
     * constructor
     * @param m
     */
    public SIZE(byte m) {
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
