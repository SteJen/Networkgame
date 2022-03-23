package SimpleEdition;

import java.io.IOException;
import java.net.Socket;


public class TCPClient {
	private StreamReader clientReadThread;
	private StreamWriter clientWriter;
	private Socket clientSocket;
	private String playerName;

	public TCPClient(String ip, int port, String playerName){
		this.playerName = playerName;
		try {
			clientSocket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//starting threads
		clientReadThread = new StreamReader(clientSocket);
		clientReadThread.start();

		clientWriter = new StreamWriter(clientSocket);

	}

	public StreamReader getClientReadThread() {
		return clientReadThread;
	}

	public StreamWriter getClientWriter() {
		return clientWriter;
	}


	public void write(String msg) {

		clientWriter.write(msg);
	}

	public String getPlayerName() {
		return playerName;
	}
}
