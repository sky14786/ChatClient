package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Model {

	private static Socket socket;
	private static DataInputStream input;
	private static DataOutputStream output;
	private String sendmessage, receivemessage, tempmessage = " ";

	public void StartServer(String ip, String port, String nickname) throws IOException {
		socket = new Socket(ip, Integer.parseInt(port));
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
		output.writeUTF(nickname);
	}

	public void StopServer() {
		try {
			String msg= "Connection terminatation!";
			Send(msg);
			socket.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public Boolean CheckConnect() {
		return socket.isConnected();
	}

	public void Send(String msg) {
		sendmessage = msg;
		try {
			output.writeUTF(sendmessage + "\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Receive() {
		try {
			while (true) {
				receivemessage = input.readUTF();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String GetMessage() {
		return receivemessage;
	}

	public void SetTempMessage(String tempmessage) {
		this.tempmessage = tempmessage;
	}

	public Boolean MessageDuplicateCheck(String receivemessage) {
		if (tempmessage == receivemessage) {
			return true;
		} else {
			return false;
		}

	}
}
