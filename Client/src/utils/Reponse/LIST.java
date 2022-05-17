package utils.Reponse;

public class LIST extends Response {

    private byte m;
    private byte s;

    public LIST(byte _m, byte _s) {
        this.m = _m;
        this.s = _s;
    }

    public byte getM() {
        return m;
    }

    public byte getS() {
        return s;
    }
}
