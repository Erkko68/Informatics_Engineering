package files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogFile {
    private final BufferedWriter log;

    public LogFile(String fileName) throws IOException {
        log = new BufferedWriter(new FileWriter(fileName));
    }

    public void close() throws IOException {
        log.close();
    }

    private void writeln(String message) throws IOException {
        log.write(message);
        log.newLine();
    }

    public void ok(String message, int lineNumber) throws IOException {
        writeln(String.format("OK(%d): %s", lineNumber, message));
    }

    public void error(String message, int lineNumber) throws IOException {
        writeln(String.format("ERROR(%d): %s", lineNumber, message));
    }
}
