package utils.Reponse;

import java.nio.ByteOrder;

public class WELCO extends Response {

    private byte m;
    private short h;
    private short w;
    private byte f;
    private String ip;
    private String port;

    public WELCO(byte _m, short _h, short _w, byte _f, String _ip, String _port) {
        this.m = _m;
        this.h = toLittleEndian(_h);
        this.w = toLittleEndian(_w);
        this.f = _f;
        this.ip = clearIP(_ip);
        this.port = _port;
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

    public byte getF() {
        return f;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public int getPortValue() {
        return Integer.parseInt(port);
    }

    static short toLittleEndian(short n) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            return n;
        } else {
            return (short) (((n >> 8) & 0xff) | ((n & 0xff) << 8));
        }
    }

    static String clearIP(String s){
        int i=0;
        while (s.charAt(i)!='#'){
            i++;
        }
        return s.substring(0,i);

    }
}
