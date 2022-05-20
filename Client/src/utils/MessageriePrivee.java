package utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class MessageriePrivee implements Runnable {

    private ConnectionHandler connectionHandler;
    private DatagramSocket datagramSocket;

    public MessageriePrivee(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;

        for (int port = 2001; port < 9999; port++ ) {
            System.out.println(port);
            try {
                this.datagramSocket = new DatagramSocket(port);
                if (this.datagramSocket.isBound()) {
                    System.out.println("Bound to : " + this.datagramSocket.getLocalPort());
                    break;
                }
                else System.out.println("Port " + port + " already in use.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public int getPort() {
        return this.datagramSocket.getLocalPort();
    }


    @Override
    public void run() {

        while (true){
            byte[] buf = new byte[218];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                datagramSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            String received = new String(
                    packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
            System.out.println(received);
            String command = received.substring(0, received.length() - 3);
            System.out.println("RECU PAR UDP : " + command);
            this.connectionHandler.exec(command);
        }
    }
}
