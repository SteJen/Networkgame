package SimpleEdition;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String[] args) throws Exception {
		ServerSocket welcomSocket = new ServerSocket(6666);
		Socket connectionSocket = welcomSocket.accept();

		StreamReader serverReadThread = new StreamReader(connectionSocket);
		serverReadThread.start();

		StreamWriter serverWriteThread = new StreamWriter("ServerWriter",connectionSocket);
		serverWriteThread.start();
	}
}
