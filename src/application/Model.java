package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class Model {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private String nickname, sendmessage, receivemessage, identity;
	private StringTokenizer msg;

	public void Connect(String ip, String nickname, int port) {

		try {
			this.nickname = nickname;
			socket = new Socket(ip, port);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			output.writeUTF(nickname);
			output.flush();

			while (true) {
				msg = new StringTokenizer(input.readUTF(), ":");
				identity = msg.nextToken();
				if (identity.equals("0001")) {
					SetReceiveMessage(msg.nextToken());
				} else if (identity.equals("0000")) {

				}

			}

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public void DisConnect() {
		try {
			socket.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void SendMessage(String msg) {

		try {
			sendmessage = "1001:" + msg + "\n";
			output.writeUTF(sendmessage);
			output.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void SetReceiveMessage(String message) {
		receivemessage = message;
	}

	public String GetReceiveMessaage() {
		return receivemessage;
	}

	public Boolean GetStatus() {
		return socket.isConnected();
	}
}

class MThread extends Thread {
	Model model = new Model();
	String ip, nickname;
	int port;

	MThread(String ip, String nickname, int port) {
		this.ip = ip;
		this.nickname = nickname;
		this.port = port;
	}

	public void run() {
		model.Connect(ip, nickname, port);
	}
}
