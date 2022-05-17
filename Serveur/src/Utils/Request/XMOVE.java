package Utils.Request;

public class XMOVE extends Request {
     private String d;

    public XMOVE(String d) {
        this.d = d;
    }

    public String getD() {
        return d;
    }

    public int getDValue() {
        return Integer.parseInt(d);
    }
}
