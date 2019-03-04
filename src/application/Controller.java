package application;

import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	private Model model = new Model();

	@FXML
	private TextArea TAdisplay;
	@FXML
	private TextField TFip, TFport, TFnickname, TFmessage;
	@FXML
	private Button BTNtgl, BTNsend;

	@FXML
	private void OpenClose(ActionEvent event) {
		try {
			if (BTNtgl.getText().equals("START")) {
				model.StartServer(TFip.getText(), TFport.getText(), TFnickname.getText());
				if (model.CheckConnect()) {
					TAdisplay.appendText("SERVER Connect Success!");
					BTNtgl.setText("STOP");
//					PrintDisplay();
				}
			} else {
				TAdisplay.appendText("Connection termination!\n");
				model.StopServer();
				BTNtgl.setText("START");
			}
		}

		catch (IOException e) {
		}
	}

	@FXML
	private void Send() {
		model.Send(TFmessage.getText());
		TFmessage.setText("");
	}

	public void PrintDisplay() {
		while (true) {
			if (model.GetMessage() != null && !model.MessageDuplicateCheck(model.GetMessage())) {
				TAdisplay.appendText(model.GetMessage() + "\n");
				model.SetTempMessage(model.GetMessage());
			}
		}
	}

}

class PhysicalReceive extends Thread {
	Model model = new Model();

	public void run() {
		model.Receive();
	}
}

class DisplayReceive extends Thread {
	Controller controller = new Controller();

	public void run() {
		controller.PrintDisplay();
	}
}
