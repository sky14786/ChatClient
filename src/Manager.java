import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

class Manager {
	private ServerSocket serversocket;
	private Socket socket;
	private ArrayList<Socket> clients;
	private HashMap<Socket, String> hm;
	private DataOutputStream output;
	private DataInputStream input;
	private String nickname, sendmessage, receivemessage,identity;
	private ArrayList<String> nicknames;
	private Boolean isduplicate;
	private ArrayList<DataInputStream> inputs;
	private ArrayList<DataOutputStream> outputs;
	private StringTokenizer temp;

//	public void PhysicalConnect() {
//		try {
//			serversocket = new ServerSocket(8000);
//			while (true) {
//				socket = serversocket.accept();
//				input = new DataInputStream(socket.getInputStream());
//				output = new DataOutputStream(socket.getOutputStream());
//				nickname = input.readUTF();
//				if (!DuplicateCheck()) {
//					LogicalConnect();
//				} else {
//					sendmessage = "0000:nick_name Duplicate Error!";
//					output.writeUTF(sendmessage + "\n");
//					output.flush();
//					socket.close();
//				}
//
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

	public void Connect() {
		try {
			serversocket = new ServerSocket(8000);
			while(true) {
				socket = 
			}
		}
	}
	
	
	public void LogicalConnect() {

		try {
			clients.add(socket);
			nicknames.add(nickname);
			hm.put(socket, nickname);
			inputs.add(input);
			outputs.add(output);
			sendmessage = "0001:Welcome to my ChatServer!";
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