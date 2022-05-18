package utils;

import utils.Reponse.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class RepParser {

    private InputStream inputStream;
    private String delim;

    public RepParser(InputStream _inputStream, String _delim) {
        this.inputStream = _inputStream;
        this.delim = _delim;
    }

    private String typeRep() throws IOException {
        byte[] reply = new byte[5];
        this.inputStream.read(reply);
        return new String(reply);
    }

    private void endLine() throws IOException {
        for (int i = 0; i < 3; i++) {
            if (this.inputStream.read() < 0) {
                System.out.println("PAS COOL");
            }
        }
    }

    private GAMES parseGAMES() throws IOException {
        byte[] tab = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(tab); //n
        endLine();

        return new GAMES(tab[0]);
    }

    private OGAME parseOGAME() throws IOException {
        byte[] m = new byte[1];
        byte[] s = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        this.inputStream.read(); //space
        this.inputStream.read(s);
        endLine();

        return new OGAME(m[0], s[0]);
    }

    private REGOK parseREGOK() throws IOException {
        byte[] m = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        endLine();

        return new REGOK(m[0]);
    }

    private UNROK parseUNROK() throws IOException {
        byte[] m = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        endLine();

        return new UNROK(m[0]);
    }

    private SIZE parseSIZE() throws IOException {
        /// TODO : s'assurer de l'ENDIANNESS
        byte[] m = new byte[1];
        byte[] h = new byte[2];
        byte[] w = new byte[2];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        this.inputStream.read(); // space
        this.inputStream.read(h);
        this.inputStream.read(); // space
        this.inputStream.read(w);
        endLine();

        return new SIZE(m[0], ByteBuffer.wrap(h).getShort(), ByteBuffer.wrap(w).getShort());
    }

    private LIST parseLIST() throws IOException {
        byte[] m = new byte[1];
        byte[] s = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        this.inputStream.read(); // space
        this.inputStream.read(s);
        endLine();

        return new LIST(m[0], s[0]);
    }

    private PLAYR parsePLAYR() throws IOException {
        byte[] id = new byte[8]; // On considère de l'UTF 8

        this.inputStream.read(); //space
        this.inputStream.read(id);
        endLine();
        return new PLAYR(new String(id));

    }

    private WELCO parseWELCO() throws IOException {
        // TODO : endianness
        byte[] m = new byte[1];
        byte[] h = new byte[2];
        byte[] w = new byte[2];
        byte[] f = new byte[1];
        byte[] ip = new byte[15];
        byte[] port = new byte[4];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        this.inputStream.read(); //space
        this.inputStream.read(h);
        this.inputStream.read(); //space
        this.inputStream.read(w);
        this.inputStream.read(); //space
        this.inputStream.read(f);
        this.inputStream.read(); //space
        this.inputStream.read(ip);
        this.inputStream.read(); //space
        this.inputStream.read(port);
        endLine();

        return new WELCO(m[0], ByteBuffer.wrap(h).getShort(),
                        ByteBuffer.wrap(w).getShort(), f[0],
                        new String(ip), new String(port));
    }

    private POSIT parsePOSIT() throws IOException {
        byte[] id = new byte[8];
        byte[] x = new byte[3];
        byte[] y = new byte[3];

        this.inputStream.read(); //space
        this.inputStream.read(id);
        this.inputStream.read(); //space
        this.inputStream.read(x);
        this.inputStream.read(); //space
        this.inputStream.read(y);
        endLine();

        return new POSIT(new String(id), new String(x), new String(y));
    }

    private MOVE parseMOVE() throws IOException {
        byte[] x = new byte[3];
        byte[] y = new byte[3];

        this.inputStream.read(); //space
        this.inputStream.read(x);
        this.inputStream.read(); //space
        this.inputStream.read(y);
        endLine();

        return new MOVE(new String(x), new String(y));
    }

    private GLIS parseGLIS() throws IOException {
        byte[] s = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(s);
        endLine();

        return new GLIS(s[0]);
    }

    private GPLYR parseGPLYR() throws IOException {
        byte[] id = new byte[8];
        byte[] x = new byte[3];
        byte[] y = new byte[3];
        byte[] p = new byte[4];

        this.inputStream.read(); //space
        this.inputStream.read(id);
        this.inputStream.read(); //space
        this.inputStream.read(x);
        this.inputStream.read(); //space
        this.inputStream.read(y);
        this.inputStream.read(); //space
        this.inputStream.read(p);
        endLine();

        return new GPLYR(new String(id), new String(x), new String(y), new String(p));

    }

    private MOVEF parseMOVEF() throws IOException {
        byte[] x = new byte[3];
        byte[] y = new byte[3];
        byte[] p = new byte[4];

        this.inputStream.read(); //space
        this.inputStream.read(x);
        this.inputStream.read(); //space
        this.inputStream.read(y);
        this.inputStream.read(); //space
        this.inputStream.read(p);
        endLine();

        return new MOVEF(new String(x), new String(y), new String(p));
    }

    public String parse() throws IOException, IllegalArgumentException {
        String type = typeRep();
        switch (type) {
            case "GAMES":
                GAMES games = parseGAMES();
                return "GAMES " + games.getN();
            case "OGAME":
                OGAME ogame = parseOGAME();
                return "OGAME " + ogame.getM() + " " + ogame.getS();
            case "REGOK":
                REGOK regok = parseREGOK();
                return "REGOK " + regok.getM();
            case "UNROK":
                UNROK unrok = parseUNROK();
                return "UNROK " + unrok.getM();
            case "SIZE!":
                SIZE size = parseSIZE();
                return "SIZE! " + size.getM() + " " + size.getH() + " " + size.getW();
            case "LIST!":
                LIST list = parseLIST();
                return "LIST! " + list.getM() + " " + list.getS();
            case "PLAYR":
                PLAYR playr = parsePLAYR();
                return "PLAYR " + playr.getId();
            case "PLJND":
                PLAYR pljnd = parsePLAYR();
                return "PLJND " + pljnd.getId();
            case "PQUIT":
                PLAYR pquit = parsePLAYR();
                return "PQUIT " + pquit.getId();
            case "PSTAT":
                PLAYR pstat = parsePLAYR();
                return "PSTAT " + pstat.getId();
            case "WELCO":
                WELCO welco = parseWELCO();
                return "WELCO " + welco.getM() + " " + welco.getH() + " " + welco.getW() + " " + welco.getF()
                        + " " + welco.getIp() + " " + welco.getPort();
            case "POSIT":
                POSIT posit = parsePOSIT();
                return "POSIT " + posit.getId() + " " + posit.getX() + " " + posit.getY();
            case "GLIS!" :
                GLIS glis = parseGLIS();
                return "GLIS " + glis.getS();
            case "GPLYR" :
                GPLYR gplyr = parseGPLYR();
                return "GPLYR " + gplyr.getId() + " " + gplyr.getX() + " " + gplyr.getY() + " " + gplyr.getP();
            case "MOVE!":
                MOVE move = parseMOVE();
                return "MOVE! " + move.getX() + " " + move.getY();
            case "MOVEF":
                MOVEF movef = parseMOVEF();
                return "MOVEF " + movef.getX() + " " + movef.getY() + " " + movef.getP();
            case "REGNO":
            case "DUNNO":
            case "SEND!":
            case "NSEND":
            case "MALL!":
            case "GOBYE":
            case "UPGD!":
                endLine();
                return type;
            default:
                endLine();
                throw new IllegalArgumentException("Unrecognized response");

        }
    }
}
