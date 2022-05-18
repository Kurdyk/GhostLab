package Utils;

import java.io.IOException;
import java.io.InputStreamReader;

public class MyBufferedReader {

    private final InputStreamReader inputStreamReader;
    private final char delim;

    public MyBufferedReader(InputStreamReader _inputStreamReader, char _delim) {
        this.inputStreamReader = _inputStreamReader;
        this.delim = _delim;
    }

    public synchronized String readInstruction() throws IOException {
        StringBuilder res = new StringBuilder();
        boolean found = false;
        while (true) {
            char c = (char) this.inputStreamReader.read();
            System.out.println(c);
            char d;
            char e;
            if (c == delim) {
                d = (char) this.inputStreamReader.read();
                e = (char) this.inputStreamReader.read();
                if (e == delim && d == delim) {
                    found = true;
                }
                if (found) {
                    System.out.println("AU FINAL " + res);
                    return res.toString();
                } else {
                    res.append(c + d + e);
                }
            } else {
                res.append(c);
            }
        }
    }
}
