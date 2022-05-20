package Utils.Request;

public class CUSPL extends Request {

    private String id;
    private String port;
    private byte dimX;
    private byte dimY;
    private byte nbGhost;
    private byte nbPlayers;

    public CUSPL(String id, String port, byte dimX, byte dimY, byte nbGhost, byte nbPlayers) {
        this.id = id;
        this.port = port;
        this.dimX = dimX;
        this.dimY = dimY;
        this.nbGhost = nbGhost;
        this.nbPlayers = nbPlayers;
    }

    public String getId() {
        return id;
    }

    public String getPort() {
        return port;
    }

    public byte getDimX() {
        return dimX;
    }

    public byte getDimY() {
        return dimY;
    }

    public byte getNbGhost() {
        return nbGhost;
    }

    public byte getNbPlayers() {
        return nbPlayers;
    }

    public int getPortValue() {
        return Integer.parseInt(port);
    }

    public int getDimXValue() {
        return (dimX);
    }

    public int getDimYValue() {
        return (dimY);
    }

    public int getNbGhostValue() {
        return (nbGhost);
    }

    public int getNbPlayersValue() {
        return (nbPlayers);
    }

}
