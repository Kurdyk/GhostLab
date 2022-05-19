package Models.Games;

import Utils.ClientHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

public class Messagerie {

    ///Clients liste
    private CopyOnWriteArrayList<ClientHandler> players;

    ///Multicast
    private InetAddress multicastIP;
    private DatagramSocket datagramSocket;
    private int multicastPort = 6789;

    public Messagerie(int id, CopyOnWriteArrayList<ClientHandler> players) throws Exception {
        this.datagramSocket = new DatagramSocket();
        this.multicastIP = InetAddress.getByName("235.0.0." + id);
        this.players = players;
    }



    public String getIpProto(){
        String s= fillIP(multicastIP.getHostAddress());
        return s;
    }

    public static String fillIP(String s) {
        System.out.println("Remplissage de : " + s);
        while (s.length() < 15) {
            System.out.println(s);
            s+="#";
        }
        return s;
    }

    public int getMulticastPort() {
        return this.multicastPort;
    }

    /**
     * Envoie un message au joueur dont l'identifiant est passé en paramètre
     * @param message le message à envoyer
     * @param playerId l'identifiant du joueur destinataire
     * @throws Exception En cas d'absence du destinataire de la partie ou en cas de problème d'envoi du paquet
     */
    public void sendToOne(String message, String playerId) throws Exception {
        ClientHandler cible = null;
        for (ClientHandler client : players) {
            if (client.getUsername().equals(playerId)) {
                cible = client;
                break;
            }
        }
        if (cible == null) {
            throw new Exception("Player not found in this game.");
        }

        String all = message + "+++";
        byte[] data = all.getBytes(StandardCharsets.UTF_8);

        DatagramPacket packet = new DatagramPacket(data, data.length, cible.getIp(), cible.getClient().getPort_udp());
        this.datagramSocket.send(packet);
    }

    /**
     * Multicast un message sur le port et l'ip choisi
     * @param message le message à envoyer.
     */
    public void multicastMessage(String message) {
        String all = message + "+++";
        byte[] data = all.getBytes(StandardCharsets.UTF_8);

        DatagramPacket packet = new DatagramPacket(data, data.length, this.multicastIP, this.multicastPort);
        try {
            this.datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
