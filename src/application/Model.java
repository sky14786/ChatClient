package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Model {

	
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	public void START_SERVER(String ip, String port, String nick_name) throws IOException {
		socket = new Socket(ip,Integer.parseInt(port));
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
			
	

	}

	public void STOP_SERVER() {

	}
	
	public void receive() throws IOException {
		String receive_msg = input.readUTF();
	}
	public void send(String msg) throws IOException {
		output.writeUTF(msg+"\n");
		output.flush();
	}
}
