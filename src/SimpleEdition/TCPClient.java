package SimpleEdition;

import java.io.IOException;
import java.net.Socket;


public class TCPClient {
	private Socket clientSocket;
	private StreamReader clientReadThread;
	private StreamWriter clientWriteThread;

	public TCPClient(String host, int port) {

		try {
			clientSocket = new Socket(host, port);

			clientReadThread = new StreamReader(clientSocket);
			clientReadThread.start();

			clientWriteThread = new StreamWriter("clientWriter", clientSocket);
			clientWriteThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StreamWriter getClientWriteThread() {
		return clientWriteThread;
	}

	/*public static void main(String[] args) throws Exception, IOException {
		// dns forespørgsel
		*//*DnsClient dnsClient = new DnsClient("localhost");
		String ip = dnsClient.translateIpToDnsName("localhost");
		System.out.println("requested ip: " +ip);
		dnsClient.stopClient();*//*

		// Tcp delen

	}*/
}
