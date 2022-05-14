package utils;

import java.io.IOException;
import java.io.InputStreamReader;

public class MyBufferedReader {

    private final InputStreamReader inputStreamReader;
    private final char delim;

    public MyBufferedReader(InputStreamReader _inputStreamReader, char _delim) {
        this.inputStreamReader = _inputStreamReader;
        this.delim = _delim;
    }

    public String readInstruction() throws IOException {
        String res = "";
        boolean found = false;
        while (true) {
            char c = (char) this.inputStreamReader.read();
            char d;
            char e;
            if (c == delim) {
                d = (char) this.inputStreamReader.read();
                e = (char) this.inputStreamReader.read();
                for (int i = 0; i < 2; i++) {
                    if (e == delim && d == delim) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    return res;
                } else {
                    res += c + d + e;
                }
            } else {
                res += c;
            }
        }
    }
}
