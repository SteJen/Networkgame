package SimpleEdition;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {
	private static ArrayList<Socket> clientConSockets = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		ServerSocket welcomSocket = new ServerSocket(6666);


		while (true){
			Socket connectionSocket = welcomSocket.accept();
			String ip = connectionSocket.getInetAddress().toString();

			boolean isNewConnect = newConnectCheck(ip);

			if (isNewConnect) {

				addClient(connectionSocket);
				StreamReader serverReadThread = new StreamReader(connectionSocket);
				serverReadThread.start();

				StreamWriter serverWriteThread = new StreamWriter("ServerWriter",connectionSocket);
				serverWriteThread.start();

			}

		}
	}

	private static boolean newConnectCheck(String ip) {
		boolean isNewConnect = true;

		for (Socket s : clientConSockets) {
			String exsistingIp = s.getInetAddress().toString();
			if (ip.contains(exsistingIp)) {
				isNewConnect = false;
			}
		}
		return isNewConnect;
	}

	public static void addClient(Socket conSocket){
		if (!clientConSockets.contains(conSocket)) {
			clientConSockets.add(conSocket);
			System.out.println(clientConSockets);
		}
	}
}
