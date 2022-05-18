package utils.Messages;

public class GHOST {

    private String x;
    private String y;

    public GHOST(String x, String y) {
        this.x = x;
        this.y = y;
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
