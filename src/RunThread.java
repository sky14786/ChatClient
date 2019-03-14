import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

class RunThread extends Thread {
	private Manager manager;
	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private String nickname, sendmessage, receivemessage, identity;
	private HashMap<Socket, String> hm = new HashMap<>();
	private ArrayList<Socket> clients = new ArrayList<>();

	RunThread(Socket socket, Manager manager) {
		this.manager = manager;
		this.socket = socket;
		hm = manager.GetHashMap();
		clients = manager.GetSocketList();

	}

	public void run() {
		StringTokenizer temp;
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			temp = new StringTokenizer(input.readUTF(), ":");
			identity = temp.nextToken();
			if (identity.equals("0000")) {
				nickname = temp.nextToken();
				if (!manager.DuplicateCheck(nickname)) {
					sendmessage = "1000:SERVER:서버 접속 성공!";
					output.writeUTF(sendmessage);
					output.flush();

					synchronized (this) {
						manager.AddSocketList(socket);
						manager.PutHashValue(socket, nickname);
						manager.AddNickNames(nickname);
					}

					ReceiveMessage();
				} else {
					sendmessage = "1000:SERVER:서버 접속 실패 ID 중복 오류!";
					output.writeUTF(sendmessage);
					output.flush();
					socket.close();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ReceiveMessage() {
		try {
			while (true) {
				receivemessage = input.readUTF();
				System.out.println(receivemessage + "\n");
				StringTokenizer temp = new StringTokenizer(receivemessage, ":");
				identity = temp.nextToken();
				if (identity.equals("1000")) {
					SendMessage(receivemessage);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SendMessage(String msg) {
		sendmessage = msg;
		for (int j = 0; j < clients.size(); j++) {
			try {
				output = new DataOutputStream(clients.get(j).getOutputStream());
				output.writeUTF(sendmessage);
				output.flush();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
}
