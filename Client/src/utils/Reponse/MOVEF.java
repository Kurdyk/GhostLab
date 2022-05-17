package utils.Reponse;

public class MOVEF extends Response {

    private String x;
    private String y;
    private String p;

    public MOVEF(String x, String y, String p) {
        this.x = x;
        this.y = y;
        this.p = p;
    }

    public String getX() {
        return x;
    }

    public int getXValue() {
        return Integer.parseInt(x);
    }

    public String getY() {
        return y;
    }

    public int getYValue() {
        return Integer.parseInt(y);
    }

    public String getP() {
        return p;
    }

    private int getPValue() {
        return Integer.parseInt(p);
    }
}
