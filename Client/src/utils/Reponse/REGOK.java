package utils.Reponse;

public class REGOK extends Response {

    private byte m;

    public REGOK(byte _m) {
        this.m = _m;
    }

    public byte getM() {
        return m;
    }
}
