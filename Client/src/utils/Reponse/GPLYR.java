package utils.Reponse;

public class GPLYR extends Response {

    private String id;
    private String x;
    private String y;
    private String p;

    public GPLYR(String id, String x, String y, String p) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.p = p;
    }

    public String getId() {
        return id;
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

    public int getPValue() {
        return Integer.parseInt(p);
    }
}
