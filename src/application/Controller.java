package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

	@FXML
	private TextArea TAdisplay;
	@FXML
	private TextField TFport, TFip, TFnick, TFinput;
	@FXML
	private Button BTNtoggle, BTNsned;

	private Socket socket;
	private String sendmessage = new String();
	private DataInputStream input;
	private DataOutputStream output;
	private StringTokenizer temp;
	private String identity = new String();
	private String receivemessage = new String();
	private String sender = new String();

	private Model model = new Model();

	private ConnectThread connectthread;

	@FXML
	public void StartStop(ActionEvent e) {
		if (BTNtoggle.getText().equals("START")) {
			model.SetNickName(TFnick.getText());
			model.SetSocket(TFip.getText(), TFport.getText());
			connectthread = new ConnectThread(this);
			connectthread.start();

			BTNtoggle.setText("STOP");
		} else {
			Stop();
			TAdisplay.appendText("Connect Close..\n");
			BTNtoggle.setText("START");

		}
	}

	@FXML
	public void SendMessage() {
		String receiver;
		String mode;
		String msg;
		if (TFinput.getText().length() != 0) {
			StringTokenizer temp2 = new StringTokenizer(TFinput.getText(), " ");
			mode = temp2.nextToken();
			if (mode.equals("/to")) {
				receiver = temp2.nextToken();
				msg = temp2.nextToken();
				WhisperSend(receiver,msg);
				TAdisplay.appendText("[To]"+receiver+" : "+msg+"\n");
				TFinput.setText(mode+" "+receiver+" ");
				
			}

			else {
				Send(TFinput.getText());
				TFinput.setText("");
			}

		}
	}

	@FXML
	public void DisplayAppend(String msg) {
		TAdisplay.appendText(msg);
	}

	public void Start() {
		try {
			String nickname = model.GetNickNmae();
			String ip = model.GetIP();
			int port = Integer.parseInt(model.GetPort());

			socket = new Socket(ip, port);
			if (socket.isConnected()) {
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				sendmessage = "0000:" + nickname;
				output.writeUTF(sendmessage);
				output.flush();

				while (true) {
					String temp2 = input.readUTF();
					if (temp2 != null) {
						temp = new StringTokenizer(temp2, ":");
						identity = temp.nextToken();
						if (identity.equals("1000")) {
							sender = temp.nextToken();
							receivemessage = temp.nextToken();
							DisplayAppend(sender + " : " + receivemessage + "\n");
						}
						else if(identity.equals("1100")) {
							sender = temp.nextToken();
							receivemessage = temp.nextToken();
							DisplayAppend("[From]"+sender+" : "+receivemessage+"\n");
						}
					}
				}

			} else {
				TAdisplay.appendText("Server Connect Fail!");
				socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void Stop() {
		try {
			input.close();
			output.close();
			socket.close();
			connectthread.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void Send(String msg) {
		try {
			sendmessage = "1000:" + model.GetNickNmae() + ":" + msg;
			output.writeUTF(sendmessage);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void WhisperSend(String receiver, String msg) {
		try {
			sendmessage = "1100:"+model.GetNickNmae()+":"+receiver+":"+msg;
			output.writeUTF(sendmessage);
			output.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

class ConnectThread extends Thread {
	Controller controller = new Controller();

	ConnectThread(Controller c) {
		controller = c;
	}

	public void run() {
		controller.Start();
	}
}
