package utils.Reponse;

public class POSIT extends Response {

    private String id;
    private String x;
    private String y;

    public POSIT(String id, String x, String y) {
        this.id = id;
        this.x = x;
        this.y = y;
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

    public int getYValue() {
        return Integer.parseInt(y);
    }

    public String getY() {
        return y;
    }
}
