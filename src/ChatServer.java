import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer {
	private static ConnectThread connectthread;
	private static ReceiveThread receivethread;

	public static void main(String[] args) {
		connectthread = new ConnectThread();
		receivethread = new ReceiveThread();
		connectthread.run();
		receivethread.run();
	}

}

class Manager {
	private ServerSocket serversocket;
	private Socket socket;
	private ArrayList<Socket> clients;
	private HashMap<Socket, String> hm;
	private DataOutputStream output;
	private DataInputStream input;
	private String nickname, sendmessage, receivemessage;
	private ArrayList<String> nicknames;
	private Boolean isduplicate;
	private ArrayList<DataInputStream> inputs;
	private ArrayList<DataOutputStream> outputs;

	public void PhysicalConnect() {
		try {
			serversocket = new ServerSocket(8000);
			while (true) {
				socket = serversocket.accept();
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				nickname = input.readUTF();
				if (!DuplicateCheck()) {
					LogicalConnect();
				} else {
					sendmessage = "NICK NAME DUPLICATED ERROR";
					output.writeUTF(sendmessage + "\n");
					output.flush();
					socket.close();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void LogicalConnect() {

		try {
			clients.add(socket);
			nicknames.add(nickname);
			hm.put(socket, nickname);
			inputs.add(input);
			outputs.add(output);
			sendmessage = "[WELCOME MY SERVER]";
			output.writeUTF(sendmessage + "\n");
			output.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public Boolean DuplicateCheck() {
		isduplicate = false;
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
		while (true) {
			for (int i = 0; i < clients.size(); i++) {
				try {
					if (inputs.get(i).readUTF() != null) {
						receivemessage = inputs.get(i).readUTF();
						SendMessage(receivemessage);
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
	}

	public void SendMessage(String msg) {
		sendmessage = msg + "\n";
		for (int j = 0; j < clients.size(); j++) {
			try {
				outputs.get(j).writeUTF(sendmessage);
				outputs.get(j).flush();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

}

class ConnectThread extends Thread {
	private Manager manager;

	ConnectThread() {
		manager = new Manager();
	}

	public void run() {
		manager.PhysicalConnect();
	}
}

class ReceiveThread extends Thread{
	private Manager manager;
	
	ReceiveThread(){
		manager = new Manager();
	}
	public void run() {
		manager.ReceiveMessage();
	}
}
