import java.util.*;
import java.net.*;
import java.io.*;

public class ChatServer {
	static ServerSocket serversocket;
	static ArrayList<Socket> clients = new ArrayList<>();
	static HashMap<Socket, Integer> hm = new HashMap<>();
	static Socket socket;

	public static void main(String[] args) throws IOException {
		serversocket = new ServerSocket(8000);
		socket = new Socket();
		socket = serversocket.accept();
		clients.add(socket);
		hm.put(socket, clients.size() - 1);

		Connect connect = new Connect(socket);
		connect.start();

	}

//	public void runserver() throws IOException {
//		Runnable runConnect = new Connect();
//		Thread RunConnectThread = new Thread(runConnect);
//		RunConnectThread.start();
//
//		Runnable sendmessage = new SendMessage(ArrayList<Socket> clients);
//		Thread RunSendMessagethread = new Thread(sendmessage);
//
//	}

}
