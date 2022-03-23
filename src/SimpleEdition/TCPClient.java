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

		clientWriter.write(msg + "," + playerName);
	}
	/*public static void main(String[] args) throws Exception, IOException {


		/*System.out.println("press a s d w s to send left right up down commands command should be followed by enter");
		ScanCharFromKeyBoard charScanner = new ScanCharFromKeyBoard(clientWriter);
		charScanner.run();*/

		/*BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = inFromUser.readLine();
			clientWriteThread.write(line);
		}
	}*/
}
