import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	private static ConnectThread connectthread;
//	private static ReceiveThread receivethread;
	private static ServerSocket serversocket;
	private static Socket socket;
	private static Manager manager = new Manager();

	public static void main(String[] args) {
		try {
			serversocket = new ServerSocket(8000);
			while (true) {
				socket = new Socket();
				socket = serversocket.accept();
				if (socket.isConnected()) {
					connectthread = new ConnectThread(socket, manager);
					connectthread.run();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

class ConnectThread extends Thread {
	private Manager manager;
	private Socket socket;

	ConnectThread(Socket socket, Manager manager) {
		this.manager = manager;
		this.socket = socket;

	}

	public void run() {
		manager.Connect(socket);
	}
}
