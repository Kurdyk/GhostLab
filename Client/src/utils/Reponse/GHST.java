package utils.Reponse;

import java.nio.ByteOrder;

public class GHST {
    private byte m;
    private short n;

    public GHST(byte _m, short _n ) {
        this.m = _m;
        this.n = toLittleEndian(_n);
    }

    public byte getM() {
        return m;
    }

    public short getN() {
        return n;
    }


    static short toLittleEndian(short n) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            return n;
        } else {
            return (short) (((n >> 8) & 0xff) | ((n & 0xff) << 8));
        }
    }
}
