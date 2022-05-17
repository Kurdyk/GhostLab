package utils.Reponse;

public class WELCO extends Response {

    private byte m;
    private short h;
    private short w;
    private byte f;
    private String ip;
    private String port;

    public WELCO(byte _m, short _h, short _w, byte _f, String _ip, String _port) {
        this.m = _m;
        this.h = _h;
        this.w = _w;
        this.f = _f;
        this.ip = _ip;
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
}
