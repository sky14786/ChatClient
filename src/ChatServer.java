import java.util.*;
import java.net.*;
import java.io.*;

public class ChatServer {
	private static Manager manager = new Manager();
	private static ConnectThread connect = new ConnectThread(manager);

	public static void main(String[] args) throws IOException {
		connect.run();

	}

}

class Manager {
	private static ServerSocket serversocket;
	private static ArrayList<Socket> clients = new ArrayList<>();
	private static HashMap<Socket, String> hm = new HashMap<>();
	private static ArrayList<String> nicknames = new ArrayList<>();
	private static String nickname, receivemessage;

	public void PhysicalConnect() {
		try {
			Socket socket;
			DataInputStream input;
			DataOutputStream output;
			serversocket = new ServerSocket(8000);
			String errormessage;
			while (true) {
				socket = serversocket.accept();
				System.out.println("Physical Connect Try...");
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				System.out.println("Physical Connect Success!");
				nickname = input.readUTF();
				System.out.println("Logical Connect Try...");
				if (DuplicateCheck(nickname)) {
					System.out.println("Logical Connect Fail!");
					errormessage = "Duplicate Check Error!\n";
					output.writeUTF(errormessage);
					output.flush();
					socket.close();
				} else {
					System.out.println("Logical Connect Success!");
					LogicalConnect(socket, nickname);
				}
			}
		} catch (IOException e) {

		}
	}

	public void LogicalConnect(Socket socket, String nickname) {
		DataInputStream input;
		DataOutputStream output;
		String sendmessage;
		try {
			clients.add(socket);
			nicknames.add(nickname);
			hm.put(socket, nickname);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			sendmessage = "Connect Success!!\n";
			output.writeUTF(sendmessage);
			output.flush();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public Boolean DuplicateCheck(String nickname) {
		Boolean isduplicate = false;
		for (int i = 0; i < nicknames.size(); i++) {
			if (nicknames.get(i).equals(nickname)) {
				isduplicate = true;
			} else {
				isduplicate = false;
			}
		}
		return isduplicate;
	}

	public void ReceiveMessage() {
		DataInputStream[] input = new DataInputStream(socket.);
		DataOutputStream[] output;
		
		while (true) {
			receivemessage=
					
		}

	}

}

class ConnectThread extends Thread {
	private Manager manager;

	ConnectThread(Manager manager) {
		this.manager = manager;
	}

	public void run() {
		manager.PhysicalConnect();
	}
}
