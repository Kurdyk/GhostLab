package Utils.Request;

public class LIST extends Request {

    private byte m;

    public LIST(byte m) {
        this.m = m;
    }

    public byte getM() {
        return m;
    }
}