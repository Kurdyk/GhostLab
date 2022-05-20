package Utils;

import Apps.ConnectionHandler;
import Models.Games.Game;
import Models.Plateau;
import Utils.Request.*;

import java.io.IOException;
import java.io.InputStream;

public class RequestParser {

    private InputStream inputStream;
    private ClientHandler client;
    private ConnectionHandler mainHandler;
    private boolean goodClient = false;


    public RequestParser(InputStream inputStream, ClientHandler client, ConnectionHandler mainHandler) {
        this.inputStream = inputStream;
        this.client = client;
        this.mainHandler = mainHandler;
    }

    private void endLine() throws IOException {
        for (int i = 0; i < 3; i++) {
            if (this.inputStream.read() < 0) {
                System.out.println("PAS COOL");
            }
        }
    }

    private String typeReq() throws IOException, SocketClosedException {
        byte[] request = new byte[5];
        int r = this.inputStream.read(request);
        if (r == -1) {
            throw new SocketClosedException();
        }
        return new String(request);
    }

    private NEWPL parseNEWPL() throws IOException {
        byte[] id = new byte[8];
        byte[] port = new byte[4];

        this.inputStream.read(); //space
        this.inputStream.read(id);
        this.inputStream.read(); //space
        this.inputStream.read(port);
        endLine();

        return new NEWPL(new String(id), new String(port));
    }

    private CUSPL parseCUSPL() throws IOException {
        byte[] id = new byte[8];
        byte[] port = new byte[4];
        byte[] dimX = new byte[1];
        byte[] dimY = new byte[1];
        byte[] nbGhost = new byte[1];
        byte[] maxPlayers = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(id);
        this.inputStream.read(); //space
        this.inputStream.read(port);
        this.inputStream.read();
        this.inputStream.read(dimX);
        this.inputStream.read();
        this.inputStream.read(dimY);
        this.inputStream.read();
        this.inputStream.read(nbGhost);
        this.inputStream.read();
        this.inputStream.read(maxPlayers);
        endLine();

        return new CUSPL(new String(id), new String(port), dimX[0], dimY[0], nbGhost[0], maxPlayers[0]);
    }

    private REGIS parseREGIS() throws IOException {
        byte[] id = new byte[8];
        byte[] port = new byte[4];
        byte[] m = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(id);
        this.inputStream.read(); //space
        this.inputStream.read(port);
        this.inputStream.read(); //space
        this.inputStream.read(m);
        endLine();

        return new REGIS(new String(id), new String(port), m[0]);
    }

    private SIZE parseSIZE() throws IOException {
        byte[] m = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        endLine();

        return new SIZE(m[0]);
    }

    private LIST parseLIST() throws IOException {
        byte[] m = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(m);
        endLine();

        return new LIST(m[0]);
    }

    private MALL parseMall() throws IOException {
        StringBuilder message = new StringBuilder();
        byte[] current = new byte[1];

        this.inputStream.read(); //space

        while (true) {
            this.inputStream.read(current);
            char a = (char) current[0];
            if (a == '*') {
                this.inputStream.read(current);
                char b = (char) current[0];
                if (b == '*') {
                    this.inputStream.read(current);
                    char c = (char) current[0];
                    if (c == '*') {
                        return new MALL(message.toString());
                    } else {
                        message.append(a).append(b).append(c);
                    }
                } else {
                    message.append(a).append(b);
                }
            } else {
                message.append(a);
            }
        }

    }

    private SEND parseSEND() throws IOException {
        byte[] id = new byte[8];
        StringBuilder message = new StringBuilder();
        byte[] current = new byte[1];

        this.inputStream.read(); //space
        this.inputStream.read(id);
        this.inputStream.read(); //space

        while (true) {
            this.inputStream.read(current);
            char a = (char) current[0];
            if (a == '*') {
                this.inputStream.read(current);
                char b = (char) current[0];
                if (b == '*') {
                    this.inputStream.read(current);
                    char c = (char) current[0];
                    if (c == '*') {
                        return new SEND(new String(id), message.toString());
                    } else {
                        message.append(a).append(b).append(c);
                    }
                } else {
                    message.append(a).append(b);
                }
            } else {
                message.append(a);
            }
        }
    }

    private XMOVE parseXMOVE() throws IOException {
        byte[] d = new byte[3];

        this.inputStream.read(); //space
        this.inputStream.read(d);
        endLine();

        return new XMOVE(new String(d));
    }

    public void parse() throws IOException, SocketClosedException {
        String type = typeReq();
        System.out.print("PARSING : ");
        switch (type) {
            case "NEWPL" -> {
                NEWPL newpl = parseNEWPL();
                System.out.println("NEWPL " + newpl.getId() + " " + newpl.getPort());
                this.client.newClient();
                this.client.getClient().setPort_udp(newpl.getPortValue());
                this.client.getClient().setName(newpl.getId());
                this.client.setUsername(newpl.getId());
                Game game = new Game(this.client, this.mainHandler);
                game.addPlayer(this.client);
                System.out.println(newpl.getId() + " crée la partie : " + game.getId() + " avec le port : " + newpl.getPort());
            }
            case "CUSPL" -> {
                CUSPL cuspl = parseCUSPL();
                this.client.newClient();
                this.client.getClient().setPort_udp(cuspl.getPortValue());
                this.client.getClient().setName(cuspl.getId());
                this.client.setUsername(cuspl.getId());
                Game game = new Game(this.client, this.mainHandler, cuspl.getDimXValue(), cuspl.getDimYValue(), cuspl.getNbGhostValue(), cuspl.getNbPlayersValue());
                game.addPlayer(this.client);
                System.out.println(cuspl.getId() + " crée la partie : " + game.getId() + " avec le port : " + cuspl.getPort());
            }
            case "START" -> {
                endLine();
                System.out.println("START");
                try {
                    this.client.getClient().getGameRunning().handleStart(client);
                } catch (NullPointerException e) {
                    System.out.println("Not in a game");
                    illegalCommand();
                }
            }
            case "REGIS" -> {
                REGIS regis = parseREGIS();
                System.out.println("REGIS " + regis.getId() + " " + regis.getPort() + " " + regis.getM());
                this.client.newClient();
                this.client.getClient().setPort_udp(regis.getPortValue());
                this.client.getClient().setName(regis.getId());
                this.client.setUsername(regis.getId());
                System.out.println(regis.getId() + " s'inscrit dans la partie : " + (int) regis.getM() + " avec le port : " + regis.getPortValue());
                try {
                    Game wantedGame = mainHandler.getAvailableGamesMap().get((int) regis.getM());
                    wantedGame.addPlayer(this.client);
                } catch (Exception e) {
                    client.getWriter().send("REGNO").end();
                }
            }
            case "UNREG" -> {
                endLine();
                System.out.println("UNREG");
                try {
                    this.client.getClient().getGameRunning().removePlayer(client);
                    this.client.getClient().kill();
                } catch (Exception e) {
                    client.getWriter().send("DUNNO").end();
                }
            }
            case "GAME?" -> {
                endLine();
                System.out.println("GAME?");
                client.getWriter().send("GAMES ").send((byte) mainHandler.getAvailableGamesNumber()).end();
                for (Game g : mainHandler.getAvailableGamesMap().values().stream().filter(Game::isJoinable).toList()) {
                    client.getWriter().send("OGAME ")
                            .send((byte) g.getId())
                            .send(" ")
                            .send((byte) g.getNb_players())
                            .end();
                }
            }
            case "SIZE?" -> {
                SIZE size = parseSIZE();
                System.out.println("SIZE? " + size.getM());
                Game g = mainHandler.getAvailableGamesMap().get((int) size.getM());
                if (g == null) {
                    client.getWriter().send("DUNNO").end();
                    break;
                }
                client.getWriter()
                        .send("SIZE! ")
                        .send((byte) g.getId())
                        .send(" ")
                        .send((short) g.getDimX())
                        .send(" ")
                        .send((short) g.getDimY())
                        .end();
            }
            case "LIST?" -> {
                LIST list = parseLIST();
                System.out.println("LIST? " + list.getM());
                Game ga = mainHandler.getAvailableGamesMap().get((int) list.getM());
                client.getWriter().send("LIST! ")
                        .send(list.getM())
                        .send(" ")
                        .send((byte) ga.getNb_players())//(ga.getNb_players() == 42 ? 41 : ga.getNb_players() ));
                        .end();
                for (ClientHandler player : ga.getPlayers()) {
                    client.getWriter().send("PLAYR ")
                            .send(player.getClient().getName())
                            .end();
                }
            }
            case "MALL?" -> {
                MALL mall = parseMall();
                System.out.println("MALL? " + mall.getMessage());
                if (!client.getClient().isAlive()) {
                    client.getWriter().send("GOBYE").end();
                    client.closeConnection();
                    break;
                }
                Game gameToMessage = client.getClient().getGameRunning();
                gameToMessage.getMessagerie().multicastMessage("MESSA " + this.client.getClient().getName() + " " + mall.getMessage());
                client.getWriter().send("MALL!").end();
            }
            case "SEND?" -> {
                SEND send = parseSEND();
                System.out.println("SEND? " + send.getId() + " " + send.getMessage());
                if (!client.getClient().isAlive()) {
                    client.getWriter().send("GOBYE").end();
                    client.closeConnection();
                    break;
                }
                Game gameToMP = client.getClient().getGameRunning();
                try {
                    gameToMP.getMessagerie().sendToOne("MESSP " + this.client.getClient().getName() + " " + send.getMessage(), send.getId());
                } catch (Exception e) {
                    client.getWriter().send("NSEND").end();
                    break;
                }
                client.getWriter().send("SEND!").end();
            }
            case "GLIS?" -> {
                System.out.println("GLIS?");
                endLine();

                if (!client.getClient().isAlive()) {
                    client.getWriter().send("GOBYE").end();
                    client.closeConnection();
                    break;
                }

                Game currentGame = client.getClient().getGameRunning();
                byte s = (byte) currentGame.getNb_players();
                client.getWriter().send("GLIS! ").send(s).end();
                for (ClientHandler c : currentGame.getPlayers()) {
                    String id = c.getClient().getName();
                    System.out.println(id + " : " + c.getClient().getCoordonnees());
                    String x = Plateau.fillCoordinate(c.getClient().getCoordonnees().getX());
                    String y = Plateau.fillCoordinate(c.getClient().getCoordonnees().getY());
                    String p = Game.fillScore(c.getClient().getScore());

                    client.getWriter()
                            .send("GPLYR ")
                            .send(id)
                            .send(" ")
                            .send(x)
                            .send(" ")
                            .send(y)
                            .send(" ")
                            .send(p)
                            .end();
                }
            }
            case "IQUIT" -> {
                System.out.println("IQUIT");
                endLine();
                client.getWriter().send("GOBYE").end();
                client.getClient().getGameRunning().removePlayer(client);
                client.closeConnection();
            }
            case "UPMOV" -> {
                XMOVE upMove = parseXMOVE();
                System.out.println("UPMOVE " + upMove.getD());
                try {
                    this.client.getClient().getGameRunning().getPlateau()
                            .moveN(this.client, "UP", upMove.getDValue());
                } catch (Exception e) {
                    System.out.println("Invalid move");
                    illegalCommand();
                }
            }
            case "DOMOV" -> {
                XMOVE downMove = parseXMOVE();
                System.out.println("DOMOV " + downMove.getD());
                try {
                    this.client.getClient().getGameRunning().getPlateau()
                            .moveN(this.client, "DOWN", downMove.getDValue());
                } catch (Exception e) {
                    System.out.println("Invalid move");
                    illegalCommand();
                }
            }
            case "LEMOV" -> {
                XMOVE leftMove = parseXMOVE();
                System.out.println("LEMOV " + leftMove.getD());
                try {
                    this.client.getClient().getGameRunning().getPlateau()
                            .moveN(this.client, "LEFT", leftMove.getDValue());
                } catch (Exception e) {
                    System.out.println("Invalid move");
                    illegalCommand();
                }
            }
            case "RIMOV" -> {
                XMOVE rightMove = parseXMOVE();
                System.out.println("RIMOV " + rightMove.getD());
                try {
                    this.client.getClient().getGameRunning().getPlateau()
                            .moveN(this.client, "RIGHT", rightMove.getDValue());
                } catch (Exception e) {
                    System.out.println("Invalid move");
                    illegalCommand();
                }
            }
            case "QUITS" -> {
                endLine();
                System.out.println("QUITS");
                client.closeConnection();
                if (client.isLoggedIn()) {
                    mainHandler.usernamesSet.remove(client.getUsername());
                }
            }
            case "PING?" -> {
                endLine();
                System.out.println("PING?");
                client.getWriter().send("PING!").end();
                client.closeConnection();
            }
            case "UPGD?" -> {
                endLine();
                System.out.println("UPGD?");
                setGoodClient(true);
                client.getWriter().send("UPGD!").end();
            }
            case "" -> throw new SocketClosedException();
            default -> {
                System.out.println(type + " is illegal, calling the cops");
                System.out.println("TYPE DE reponse := " + type.getClass());
                System.out.println("TAILLE = " + type.length());
                illegalCommand();
            }
        }
    }


    private void illegalCommand(){
        client.getWriter().send("FUCKU").end();
        try {
            this.client.getClient().getGameRunning().removePlayer(client);
        } catch (Exception ignored) {}
        client.closeConnection();
        if (client.isLoggedIn()){
            mainHandler.usernamesSet.remove(client.getUsername());
        }
    }

    /**
     * Is good client boolean.
     *
     * @return the boolean
     */
    public boolean isGoodClient() {
        return goodClient;
    }

    /**
     * Sets good client.
     *
     * @param goodClient the good client
     */
    public void setGoodClient(boolean goodClient) {
        this.goodClient = goodClient;
    }

}
