
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connect extends Thread {
	Socket socket;

	public Connect(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {

			System.out.println("Client Connect Success!");

			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			output.writeUTF("Server --> Client Connection\n");
			output.flush();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
