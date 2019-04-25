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
	private TextArea taDisplay;
	@FXML
	private TextField tfPort, tfIp, tfNick, tfInput;
	@FXML
	private Button btnToggle, btnSend, btnFile;

	private Socket socket;
	private String sendMessage = new String();
	private DataInputStream input;
	private DataOutputStream output;
	private StringTokenizer temp;
	private String identity = new String();
	private String receiveMessage = new String();
	private String sender = new String();
	private boolean isWhisper;
	private String receiver;

	private Model model = new Model();

	private connectThread connectThread;

	@FXML
	public void startStop(ActionEvent e) {
		if (btnToggle.getText().equals("START")) {
			model.setNickName(tfNick.getText());
			model.setSocket(tfIp.getText(), tfPort.getText());
			connectThread = new connectThread(this);
			connectThread.start();

			btnToggle.setText("STOP");
		} else {
			Stop();
			taDisplay.appendText("Connect Close..\n");
			btnToggle.setText("START");

		}
	}

	@FXML
	public void sendMessage() {
		String mode;
		String msg;
		if (tfInput.getText().length() != 0) {
			StringTokenizer temp2 = new StringTokenizer(tfInput.getText(), " ");
			mode = temp2.nextToken();
			if (mode.equals("/to")) {
				receiver = temp2.nextToken();
				msg = temp2.nextToken();
				whisperSend(receiver, msg);
				taDisplay.appendText("[To]" + receiver + " : " + msg + "\n");
				tfInput.setText(mode + " " + receiver + " ");

			} else if (mode.equals("/tofile")) {
				receiver = temp2.nextToken();
				isWhisper = true;
				fileSearch();
			} else {
				send(tfInput.getText());
				tfInput.setText("");
			}

		}
	}

	@FXML
	private void fileSearch() {
		fileChooser();
	}

	@FXML
	public void displayAppend(String msg) {
		taDisplay.appendText(msg);
	}

	public void fileChooser() {
		fileChooser fileChooser = new fileChooser();
		Window stage = null;
		File file = fileChooser.showOpenDialog(stage);
		fileThread fileThread = new fileThread(this, file, isWhisper, receiver);
		fileThread.start();
	}

	public void start() {
		try {
			String nickName = model.getNickNmae();
			String ip = model.getIP();
			int port = Integer.parseInt(model.getPort());

			socket = new Socket(ip, port);
			if (socket.isConnected()) {
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				sendMessage = "0000:" + nickName;
				output.writeUTF(sendMessage);
				output.flush();

				while (true) {
					String temp2 = input.readUTF();
					if (temp2 != null) {
						temp = new StringTokenizer(temp2, ":");
						identity = temp.nextToken();
						if (identity.equals("1000")) {
							sender = temp.nextToken();
							receiveMessage = temp.nextToken();
							displayAppend(sender + " : " + receiveMessage + "\n");
						} else if (identity.equals("1100")) {
							sender = temp.nextToken();
							String tem = temp.nextToken();
							receiveMessage = temp.nextToken();
							displayAppend("[From]" + sender + " : " + receiveMessage + "\n");
						} else if (identity.equals("1001")) {
							sender = temp.nextToken();
							String fileName = temp.nextToken();
							displayAppend(sender + " 님이 " + fileName + " 을 보내셨습니다.\n");
//							FileReceiver(filename);
							fileReceiverThread fileReceiverThread = new fileReceiverThread(this, fileName);
							fileReceiverThread.start();
						} else if (identity.equals("1101")) {
							sender = temp.nextToken();
							String fileName = temp.nextToken();
							displayAppend("[From]" + sender + " : " + fileName + " 을 보내셨습니다.\n");
//							FileReceiver(filename);
							fileReceiverThread fileReceiverThread = new fileReceiverThread(this, fileName);
							fileReceiverThread.start();
						}
					}
				}

			} else {
				taDisplay.appendText("Server Connect Fail!");
				socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		try {
			input.close();
			output.close();
			socket.close();
			connectThread.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void send(String msg) {
		try {
			sendMessage = "1000:" + model.getNickNmae() + ":" + msg;
			output.writeUTF(sendMessage);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fileSend(File file, boolean isWhisper, String receiver) {
		try {
			if (isWhisper) {
				sendMessage = "1101:" + model.getNickNmae() + ":" + file.getName() + ":" + receiver;
			} else {
				sendMessage = "1001:" + model.getNickNmae() + ":" + file.getName();
			}

			output.writeUTF(sendMessage);
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
			if (isWhisper) {
				displayAppend("[To]" + receiver + ":" + file.getName());
			} else {
				displayAppend(file.getName() + "을 보냈습니다.");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void whisperSend(String receiver, String msg) {
		try {
			sendMessage = "1100:" + model.getNickNmae() + ":" + receiver + ":" + msg;
			output.writeUTF(sendMessage);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fileReceiver(String fileName) {
		try {
			BufferedInputStream buin = new BufferedInputStream(socket.getInputStream());
			FileOutputStream fout = new FileOutputStream(fileName);
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

class connectThread extends Thread {
	Controller controller = new Controller();

	connectThread(Controller c) {
		controller = c;
	}

	public void run() {
		controller.start();
	}
}

class fileThread extends Thread {
	Controller controller = new Controller();
	File file;
	boolean isWhisper;
	String receiver;

	fileThread(Controller c, File file, boolean isWhisper, String receiver) {
		controller = c;
		this.file = file;
		this.isWhisper = isWhisper;
		this.receiver = receiver;
	}

	public void run() {
		controller.fileSend(file, isWhisper, receiver);
	}
}

class fileReceiverThread extends Thread {
	Controller controller = new Controller();
	String fileName;

	fileReceiverThread(Controller c, String fileName) {
		controller = c;
		this.fileName = fileName;
	}

	public void run() {
		controller.fileReceiver(fileName);
	}

}
