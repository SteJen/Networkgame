package SimpleEdition;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StreamWriter extends Thread{
    private final String threadName;
    private Socket connectionSocket;
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

    public StreamWriter(String threadName, Socket serverSocket) {
        this.connectionSocket = serverSocket;
        this.threadName = threadName;
    }

    public void run() {
        while (true) {
            try {
                DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
                String usrInput = inFromUser.readLine();
                out.writeBytes(threadName+" : " +usrInput + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
