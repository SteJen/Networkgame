package SimpleEdition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StreamReaderServer extends Thread{
    private BufferedReader in;
    private CriticalZone criticalZone;

    public StreamReaderServer(Socket connectionSocket, CriticalZone criticalZone) {
        this.criticalZone = criticalZone;

        try {
            in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String data = in.readLine();
                criticalZone.push(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
