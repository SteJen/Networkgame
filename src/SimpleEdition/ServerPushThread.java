package SimpleEdition;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerPushThread extends Thread {

    private final CriticalZone criticalZone;
    private final ArrayList<Socket> inSockets;

    public ServerPushThread(CriticalZone criticalZone, ArrayList<Socket> inSockets) {
        this.criticalZone = criticalZone;
        this.inSockets = inSockets;
    }

    @Override
    public void run() {
        System.out.println("ServerPushThread Running");
        while (true) {
            try {
                pushTOClients();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void pushTOClients() throws IOException {
        //pop and return if stack is empty
        //String msg = criticalZone.pop();
        String msg = criticalZone.poll();
        if ( msg.isEmpty()) {
            return;
        }
        // else add newline and push it back to all clients
        String terminated = msg += "\n";

        for (int j = 0; j < inSockets.size(); j++) {
            inSockets.get(j).getOutputStream().write(terminated.getBytes() );
        }
    }
}
