package Utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class AudioStreamingServer implements Runnable{
    private byte[] buffer;
    private DatagramSocket socket;
    AudioInputStream inputStream;

    private final InetAddress ip = InetAddress.getByName("235.0.0.221");
    private final int port = 21901;

    public AudioStreamingServer() throws UnknownHostException {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource("../musique.wav")));
            AudioFormat format = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    16,
                    format.getChannels(),
                    format.getChannels() * 2,
                    format.getSampleRate(),
                    false);
            System.out.println(decodedFormat);
            inputStream = AudioSystem.getAudioInputStream(decodedFormat, in);

            this.buffer = new byte[(int) (decodedFormat.getSampleRate() / 4 * 4)];
            System.out.println("Buffer size = " + this.buffer.length);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            inputStream.mark(Integer.MAX_VALUE);
            long lastsent = System.currentTimeMillis();
            System.out.println("Socket created !");
            while (true){
                while (System.currentTimeMillis() - lastsent < 249) {}
                int c = inputStream.read(this.buffer, 0, this.buffer.length);
                System.out.println("Have read " + c + " bytes from buffer");
                if (c > 0) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ip, port);
                    socket.send(packet);
                    lastsent = System.currentTimeMillis();
                } else {
                    inputStream.reset();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}





















