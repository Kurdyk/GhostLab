package utils.Messages;

public class SCORE {

    private String id;
    private String p;
    private String x;
    private String y;

    public SCORE(String id, String p, String x, String y) {
        this.id = id;
        this.p = p;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public String getP() {
        return p;
    }

    public int getPValue() {
        return Integer.parseInt(p);
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
}
