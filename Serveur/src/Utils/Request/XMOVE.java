package Utils.Request;

public class XMOVE extends Request {
     private String d;

    /**
     * constructor
     * @param d
     */
    public XMOVE(String d) {
        this.d = d;
    }

    /**
     * get d
     * @return d's value
     */
    public String getD() {
        return d;
    }

    /**
     * get d's integer's value
     * @return d's integer's value
     */
    public int getDValue() {
        return Integer.parseInt(d);
    }
}
