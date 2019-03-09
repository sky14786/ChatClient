package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	MThread mthread;
	Model model = new Model();

	@FXML
	private TextArea TAdisplay;
	@FXML
	private TextField TFport, TFip, TFnick, TFinput;
	@FXML
	private Button BTNtoggle, BTNsned;

	@FXML
	private void Start_Stop(ActionEvent e) {
		if (BTNtoggle.getText().equals("START")) {
//			model.Connect(TFip.getText(), TFnick.getText(), 8000);
			mthread = new MThread(TFip.getText(), TFnick.getText(), 8000);
			mthread.run();
			if (model.GetStatus()) {
				TAdisplay.appendText("���� ���� ����!\n");
				BTNtoggle.setText("STOP");
			} else {
				TAdisplay.appendText("���� ���� ���� �г��� Ȯ�� ���!\n");
			}
		} else {
			model.DisConnect();
			TAdisplay.appendText("�������� ���� ����!\n");
		}
	}

//	@FXML
//	private void Print_Message() {
//		while (true) {
//			TAdisplay.appendText(model.GetReceiveMessaage());
//		}
//
//	}

	@FXML
	private void Send_Message(ActionEvent e) {
		if(TFinput.getText()!=null) {
			model.SendMessage(TFinput.getText());
			TFinput.setText("");
		}
	}

}
