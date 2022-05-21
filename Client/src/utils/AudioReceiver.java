package utils;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class AudioReceiver implements Runnable{
    AudioInputStream inputStream;
    SourceDataLine dataLine;

    AudioFormat audioFormat;
    Player player;
    Thread pThread;

    private boolean running = true;

    private byte[] buffer = new byte[44100];

    private ArrayBlockingQueue<AudioInputStream> queue = new ArrayBlockingQueue<>(100000, true);

    public AudioReceiver() {
        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4, 44100F, false);

    }

    public ArrayBlockingQueue<AudioInputStream> getQueue() {
        return queue;
    }

    @Override
    public void run() {
        player = new Player(this);
        pThread = new Thread(this.player);
        pThread.start();
        try {
            DataLine.Info infos = new DataLine.Info(SourceDataLine.class, audioFormat);
            dataLine = (SourceDataLine) AudioSystem.getLine(infos);
            dataLine.open(this.audioFormat);
            dataLine.start();
            MulticastSocket socket = new MulticastSocket(21901);
            socket.joinGroup(InetAddress.getByName("235.0.0.221"));
            while (running){
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                try {
                    byte[] data = packet.getData();
                    InputStream is = new ByteArrayInputStream(Arrays.copyOfRange(buffer, 0, packet.getLength()-1));
                    queue.add(new AudioInputStream(is, audioFormat, packet.getLength()));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            socket.leaveGroup(InetAddress.getByName("235.0.0.221"));
            socket.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private class Player implements Runnable {
        private AudioReceiver handler;

        public Player(AudioReceiver handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] ai = new byte[44100];
            AudioInputStream audioInputStream;
            int c;
            try {
                while ((audioInputStream = this.handler.getQueue().poll()) != null && (c = audioInputStream.read(ai, 0, ai.length)) != -1) {
                    if (c > 0) {
                        dataLine.write(ai, 0, ai.length);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
