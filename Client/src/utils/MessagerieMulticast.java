package utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MessagerieMulticast implements Runnable {

    ///Multicast
    private final InetAddress multicastIP;
    private MulticastSocket multicastSocket;
    private final int multicastPort;

    public MessagerieMulticast(InetAddress multicastIP, int multicastPort) {
        this.multicastIP = multicastIP;
        this.multicastPort = multicastPort;
    }

    @Override
    public void run() {
        try {
            multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastIP);
            while (true) {
                byte[] buf = new byte[209];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    multicastSocket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                String received = new String(
                        packet.getData(), 0, packet.getLength());
            }
            multicastSocket.leaveGroup(multicastIP);
            multicastSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
