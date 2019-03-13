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
	private static Manager manager;

	public static void main(String[] args) {
		try {
			manager = new Manager();
			manager.ResetVariables();
//			receivethread = new ReceiveThread();
			serversocket = new ServerSocket(8000);
			while (true) {
				socket = new Socket();
				socket = serversocket.accept();
				if(socket.isConnected()) {
					connectthread = new ConnectThread(socket);
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
	ConnectThread(Socket socket) {
		manager = new Manager();
		this.socket = socket;
	}

	public void run() {
		manager.Connect(socket);
	}
}

//class ReceiveThread extends Thread {
//	private Manager manager;
//
//	ReceiveThread() {
//		manager = new Manager();
//	}
//
//	public void run() {
//		manager.ReceiveMessage();
//	}
//}
