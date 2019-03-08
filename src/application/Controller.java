package application;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	Model model = new Model();

	@FXML
	private TextArea TAdisplay;
	@FXML
	private TextField TFport, TFip, TFnick;
	@FXML
	private Button BTNtoggle, BTNsned;

	@FXML
	private void Start_Stop(ActionEvent e) {
		if(BTNtoggle.getText().equals("START")) {
			model.Connect(TFip.getText(), TFnick.getText(), 8000);
			if(model.GetStatus()) {
				TAdisplay.appendText("서버 접속 성공!\n");
				BTNtoggle.setText("STOP");
			}
			else {
				TAdisplay.appendText("서버 접속 실패 닉네임 확인 요망!\n");
			}
		}
		else {
			model.DisConnect();
			TAdisplay.appendText("서버와의 연결 종료!\n");
		}
	}

}
