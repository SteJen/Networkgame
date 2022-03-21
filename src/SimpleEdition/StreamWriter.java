package SimpleEdition;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StreamWriter {
    private DataOutputStream out;

    public StreamWriter(Socket connectionSocket) {
        try {
            out = new DataOutputStream(connectionSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void write(String msg) {
        try {
            String terminatedMsg = msg + "\n";
            System.out.println(terminatedMsg + "writer");
            out.write(terminatedMsg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
