import java.util.*;
import java.net.*;
import java.io.*;

public class ChatServer {
static ServerSocket serversocket;

	public static void main(String[] args) {
		ChatServer s = new ChatServer();
		s.runserver();

	}
	
	public void runserver() {
		try {
			System.out.println("Client Connect Hold...");
			serversocket = new ServerSocket(8000);
			
			Socket socket = serversocket.accept();
			System.out.println("Client Connect Success!");
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			output.write("Server --> Client Connection\n");
			output.flush();
			
//			String clientdata;
//			while(true) {
//				if(input.readLine()!=null) {
//					clientdata = input.readLine();
//					System.out.println("[CLIENT]"+clientdata);
//				}
//			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
