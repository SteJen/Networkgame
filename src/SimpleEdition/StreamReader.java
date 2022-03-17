package SimpleEdition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StreamReader extends Thread{
    private Socket connectionSocket;

    public StreamReader(Socket connectionSocket) {

        this.connectionSocket = connectionSocket;
    }

    public void run() {
        while (true) {
            try {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String clientSentence = inFromClient.readLine();

                System.out.println(clientSentence);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
