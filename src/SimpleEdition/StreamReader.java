package SimpleEdition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class StreamReader extends Thread{
    private Deque<String> messages = new ArrayDeque<>();
    private BufferedReader in;

    public StreamReader(Socket connectionSocket) {
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
                add(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void add(String msg) {
        messages.add(msg);
    }
    public synchronized String read() {
        return messages.poll();
    }
}
