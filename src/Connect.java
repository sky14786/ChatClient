import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Connect implements Runnable {
	static ServerSocket serversocket;

	ArrayList<Socket> clients = new ArrayList<>();
	HashMap<Socket, Integer> hm = new HashMap<>();

	@Override
	public void run() {
		Socket socket;

		try {
			serversocket = new ServerSocket(8000);
			while (true) {
				
				socket = serversocket.accept();
				clients.add(socket);
				hm.put(socket, clients.size() - 1);
				System.out.println("Client Connect Success!");

				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				output.write("Server --> Client Connection\n");
				output.flush();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
