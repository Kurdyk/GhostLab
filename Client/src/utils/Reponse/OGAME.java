package utils.Reponse;

public class OGAME extends Response {

    private byte m;
    private byte s;

    public OGAME(byte _m, byte _s) {
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
