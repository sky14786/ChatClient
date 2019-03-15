package application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class Controller {

	@FXML
	private TextArea TAdisplay;
	@FXML
	private TextField TFport, TFip, TFnick, TFinput;
	@FXML
	private Button BTNtoggle, BTNsned, BTNfile;

	private Socket socket;
	private String sendmessage = new String();
	private DataInputStream input;
	private DataOutputStream output;
	private StringTokenizer temp;
	private String identity = new String();
	private String receivemessage = new String();
	private String sender = new String();
	private boolean iswhisper;
	private String receiver;

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
		String mode;
		String msg;
		if (TFinput.getText().length() != 0) {
			StringTokenizer temp2 = new StringTokenizer(TFinput.getText(), " ");
			mode = temp2.nextToken();
			if (mode.equals("/to")) {
				receiver = temp2.nextToken();
				msg = temp2.nextToken();
				WhisperSend(receiver, msg);
				TAdisplay.appendText("[To]" + receiver + " : " + msg + "\n");
				TFinput.setText(mode + " " + receiver + " ");

			} else if (mode.equals("/tofile")) {
				receiver = temp2.nextToken();
				iswhisper = true;
				FileSearch();
			} else {
				Send(TFinput.getText());
				TFinput.setText("");
			}

		}
	}

	@FXML
	private void FileSearch() {
		FileChooser();
	}

	@FXML
	public void DisplayAppend(String msg) {
		TAdisplay.appendText(msg);
	}

	public void FileChooser() {
		FileChooser filechooser = new FileChooser();
		Window stage = null;
		File file = filechooser.showOpenDialog(stage);
		FileThread filethread = new FileThread(this, file, iswhisper, receiver);
		filethread.start();
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
						} else if (identity.equals("1100")) {
							sender = temp.nextToken();
							String tem = temp.nextToken();
							receivemessage = temp.nextToken();
							DisplayAppend("[From]" + sender + " : " + receivemessage + "\n");
						} else if (identity.equals("1001")) {
							sender = temp.nextToken();
							String filename = temp.nextToken();
							DisplayAppend(sender + " 님이 " + filename + " 을 보내셨습니다.\n");
//							FileReceiver(filename);
							FileReceiverThread filereceiverthread = new FileReceiverThread(this, filename);
							filereceiverthread.start();
						} else if (identity.equals("1101")) {
							sender = temp.nextToken();
							String filename = temp.nextToken();
							DisplayAppend("[From]" + sender + " : " + filename + " 을 보내셨습니다.\n");
//							FileReceiver(filename);
							FileReceiverThread filereceiverthread = new FileReceiverThread(this, filename);
							filereceiverthread.start();
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

	public void FileSend(File file, boolean iswhisper, String receiver) {
		try {
			if (iswhisper) {
				sendmessage = "1101:" + model.GetNickNmae() + ":" + file.getName() + ":" + receiver;
			} else {
				sendmessage = "1001:" + model.GetNickNmae() + ":" + file.getName();
			}

			output.writeUTF(sendmessage);
			output.flush();

			BufferedOutputStream buout = new BufferedOutputStream(socket.getOutputStream());
			FileInputStream fin = new FileInputStream(file);
			byte[] buffer = new byte[8096];
			int len;

			while ((len = fin.read(buffer)) != -1) {
				buout.write(buffer, 0, len);
			}

			buout.flush();
			buout.close();
			fin.close();
			output.close();
			if (iswhisper) {
				DisplayAppend("[To]" + receiver + ":" + file.getName());
			} else {
				DisplayAppend(file.getName() + "을 보냈습니다.");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void WhisperSend(String receiver, String msg) {
		try {
			sendmessage = "1100:" + model.GetNickNmae() + ":" + receiver + ":" + msg;
			output.writeUTF(sendmessage);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void FileReceiver(String filename) {
		try {
			BufferedInputStream buin = new BufferedInputStream(socket.getInputStream());
			FileOutputStream fout = new FileOutputStream(filename);
			int len;
			byte[] buffer = new byte[8096];

			while ((len = buin.read(buffer)) != -1) {
				fout.write(buffer, 0, len);
			}

			fout.flush();
			fout.close();
			buin.close();
			input.close();

		} catch (IOException e) {
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

class FileThread extends Thread {
	Controller controller = new Controller();
	File file;
	boolean iswhisper;
	String receiver;

	FileThread(Controller c, File file, boolean iswhisper, String receiver) {
		controller = c;
		this.file = file;
		this.iswhisper = iswhisper;
		this.receiver = receiver;
	}

	public void run() {
		controller.FileSend(file, iswhisper, receiver);
	}
}

class FileReceiverThread extends Thread {
	Controller controller = new Controller();
	String filename;

	FileReceiverThread(Controller c, String filename) {
		controller = c;
		this.filename = filename;
	}

	public void run() {
		controller.FileReceiver(filename);
	}

}
