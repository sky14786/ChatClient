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
				TAdisplay.appendText("���� ���� ����!\n");
				BTNtoggle.setText("STOP");
			}
			else {
				TAdisplay.appendText("���� ���� ���� �г��� Ȯ�� ���!\n");
			}
		}
		else {
			model.DisConnect();
			TAdisplay.appendText("�������� ���� ����!\n");
		}
	}

}
