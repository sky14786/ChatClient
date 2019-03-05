package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Model {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private String nickname, sendmessage, receivemessage;

	public void Connect(String ip, String nickname, int port) {

		try {
			this.nickname = nickname;
			socket = new Socket(ip, port);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			output.writeUTF(nickname);
			output.flush();

			String msg = input.readUTF();
			if (msg.equals("false")) {
				SetReceiveMessage(msg);
			} else {
				SetReceiveMessage(msg);
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

	public void SetReceiveMessage(String message) {
		receivemessage = message;
	}

	public String GetReceiveMessaage() {
		return receivemessage;
	}
}
