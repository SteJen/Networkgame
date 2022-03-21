package SimpleEdition;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer{
	private static ArrayList<Socket> INsockets = new ArrayList<>();
	private static CriticalZone criticalZone = new CriticalZone();

	public static void main(String[] args) throws Exception {
		ServerSocket welcomSocket = new ServerSocket(6666);

		// opret pushTread der sørger for at skuppe det der kommer ind fra en klient ud til de andre klienter.
		// dette gøres via en criticalZone
		ServerPushThread spth = new ServerPushThread(criticalZone, INsockets);
		spth.start();

		// venter på at klienter forbinder og opretter dem i liste af klienter INsockets
		while (true) {
			// vent på forbindelse
			Socket connectionSocket = welcomSocket.accept();
			// hiv ipaddresse ud og tjek om klient findes på serveren.
			String incommeingIp = connectionSocket.getInetAddress().toString();
			System.out.println(incommeingIp);

			// hvis client har været koblet på før slette gammel klient og ny tilføjes
			// ellers tilføjes blot en ny
			deleteOutdatedSocketFromSockets(incommeingIp);
			addClient(connectionSocket);
		}
	}

	// slettet existerende sockets, hvis nogen, der har samme ip som incomming ip
	private static void deleteOutdatedSocketFromSockets(String incommeingIp) {
		int removeIndex = 0;
		boolean found = false;
		for (int i = 0; i < INsockets.size() && !found; i++) {
			String ip = INsockets.get(i).getInetAddress().toString();
			if (ip.equals(incommeingIp)) {
				removeIndex = i;
				found = true;
			}
		}

		if (found) {
			INsockets.remove(removeIndex);
		}
	}

	// tilføjer klient
	private static void addClient(Socket connectionSocket) {
		StreamReaderServer serverReadThread = new StreamReaderServer(connectionSocket,criticalZone);
		serverReadThread.start();
		INsockets.add(connectionSocket);
		System.out.println("ConnectedClients: "+INsockets);
	}
}
