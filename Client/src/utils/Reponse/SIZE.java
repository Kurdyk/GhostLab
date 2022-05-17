package utils.Reponse;

public class SIZE extends Response {

    private byte m;
    private short h;
    private short w;

    public SIZE(byte _m, short _h, short _w) {
        this.m = _m;
        this.h = _h;
        this.w = _w;
    }

    public byte getM() {
        return m;
    }

    public short getH() {
        return h;
    }

    public short getW() {
        return w;
    }
}
