import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

	private static ServerSocket serversocket;
	private static Socket socket;
	private static Manager manager = new Manager();
	private static RunThread runthread;

	public static void main(String[] args) {
		try {
			serversocket = new ServerSocket(8000);
			while (true) {
				socket = new Socket();
				socket = serversocket.accept();
				if (socket.isConnected()) {
					runthread = new RunThread(socket, manager);
					runthread.start();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

