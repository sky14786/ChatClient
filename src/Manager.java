import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

class Manager {
	private Socket socket;
	private ArrayList<Socket> clients = new ArrayList<>();
	private HashMap<Socket, String> hm = new HashMap<>();
	private DataOutputStream output;
	private DataInputStream input;
	private String nickname, sendmessage, receivemessage, identity;
	private ArrayList<String> nicknames = new ArrayList<>();
	private Boolean isduplicate;
	private ArrayList<DataInputStream> inputs = new ArrayList<>();
	private ArrayList<DataOutputStream> outputs = new ArrayList<>();

	// 1000 일반 메시지 0000 접속시도
	public void Connect(Socket soc) {
		StringTokenizer temp;
		try {
			socket = soc;
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			temp = new StringTokenizer(input.readUTF(), ":");
			identity = temp.nextToken();
			if (identity.equals("0000")) {
				nickname = temp.nextToken();
				if (!DuplicateCheck()) {
					sendmessage = "1000:SERVER:서버 접속 성공!";
					output.writeUTF(sendmessage);
					output.flush();

					clients.add(socket);
					hm.put(socket, nickname);
					nicknames.add(nickname);
					inputs.add(input);
					outputs.add(output);

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

	public Boolean DuplicateCheck() {
		isduplicate = false;
		for (int i = 0; i < nicknames.size(); i++) {
			if (nicknames.get(i).equals(nickname)) {
				isduplicate = true;
				return isduplicate;
			} else {
				isduplicate = false;
			}
		}
		return isduplicate;
	}

	public void ReceiveMessage() {
		try {
			while (true) {
				receivemessage = input.readUTF();
				StringTokenizer temp = new StringTokenizer(receivemessage, ":");
				identity = temp.nextToken();
				if (identity.equals("1000")) {
					SendMessage(temp.nextToken());
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
				outputs.get(j).writeUTF("1000:" + nickname + ":" + sendmessage);
				outputs.get(j).flush();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

}