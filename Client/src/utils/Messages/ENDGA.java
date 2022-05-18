package utils.Messages;

public class ENDGA {

    private String id;
    private String p;

    public ENDGA(String id, String p) {
        this.id = id;
        this.p = p;
    }

    public String getId() {
        return id;
    }

    public String getP() {
        return p;
    }

    public int getPValue() {
        return Integer.parseInt(p);
    }
}
