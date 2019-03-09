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
				TAdisplay.appendText("서버 접속 성공!\n");
				BTNtoggle.setText("STOP");
			} else {
				TAdisplay.appendText("서버 접속 실패 닉네임 확인 요망!\n");
			}
		} else {
			model.DisConnect();
			TAdisplay.appendText("서버와의 연결 종료!\n");
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
