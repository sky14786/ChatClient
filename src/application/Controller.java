package application;

import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	Model model = new Model();

	@FXML
	public TextArea display;
	@FXML
	private TextField tf_ip;
	@FXML
	private TextField tf_port;
	@FXML
	private TextField tf_nick;
	@FXML
	private Button btn_tgl;

	@FXML
	private void start_stop(ActionEvent event) throws IOException {
		if (btn_tgl.getText().equals("START")) {
			model.START_SERVER(tf_ip.getText(), tf_port.getText(), tf_nick.getText());
			if (model.socket.isConnected()) {
				display.appendText("SERVER Connect Success!");
				btn_tgl.setText("STOP");
			}
		}
	}

	@FXML
	private void receive(ActionEvent event) {

	}

}
