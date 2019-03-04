package application;

import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	private PhysicalReceive physicalreceive = new PhysicalReceive();
	private DisplayReceive displayreceive = new DisplayReceive();
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("design.fxml"));
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("[CLIENT]");
			primaryStage.show();
			physicalreceive.run();
			displayreceive.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}