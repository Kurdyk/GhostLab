package utils.Reponse;

import java.nio.ByteOrder;

public class SIZE extends Response {

    private byte m;
    private short h;
    private short w;

    public SIZE(byte _m, short _h, short _w) {
        this.m = _m;
        this.h = toLittleEndian(_h);
        this.w = toLittleEndian(_w);
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

    static short toLittleEndian(short n) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            return n;
        } else {
            return (short) (((n >> 8) & 0xff) | ((n & 0xff) << 8));
        }
    }
}
