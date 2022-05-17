package utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;


public class TestWriter {

    private OutputStream outputStream;
    private String delim;

    public TestWriter(OutputStream _outputStream, String _delim) {
        this.outputStream = _outputStream;
        this.delim = _delim;
    }

    private short toLittleEndian(short n) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            return n;
        } else {
            return (short) (((n >> 8) & 0xff) | ((n & 0xff) << 8));
        }
    }

    public TestWriter send(short n) {
        n = toLittleEndian(n);
        ByteBuffer toSend = ByteBuffer.allocate(2);
        toSend.putShort(n);
        send(toSend.array());
        return this;
    }

    public TestWriter send(byte n) {
        byte[] tab = new byte[1];
        tab[0] = n;
        return this.send(tab);
    }

    public TestWriter send(int n) {
        try {
            this.outputStream.write(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public TestWriter send(byte[] bytes) {
        try {
            this.outputStream.write(bytes);
        } catch (IOException e) {
            System.out.println("Socket closed by host");
            System.exit(1);
        }
        return this;
    }

    public TestWriter send(String message) {
        return this.send(message.getBytes(StandardCharsets.UTF_8));
    }

    public void end() {
        this.send(delim.getBytes());
        try {
            this.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
