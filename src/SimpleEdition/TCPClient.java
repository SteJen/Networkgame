package SimpleEdition;

import java.io.IOException;
import java.net.Socket;


public class TCPClient {
	public static void main(String[] args) throws Exception, IOException {
		// dns forespørgsel
		/*DnsClient dnsClient = new DnsClient("localhost");
		String ip = dnsClient.translateIpToDnsName("localhost");
		System.out.println("requested ip: " +ip);
		dnsClient.stopClient();*/

		// Tcp delen
		Socket clientSocket = new Socket("10.10.131.170", 6666);
		StreamReader clientReadThread = new StreamReader(clientSocket);
		clientReadThread.start();

		StreamWriter clientWriteThread = new StreamWriter("clientWriter", clientSocket);
		clientWriteThread.start();
	}
}
