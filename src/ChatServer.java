import java.util.*;
import java.net.*;
import java.io.*;

public class ChatServer {

	public static void main(String[] args) throws IOException {
		ChatServer s = new ChatServer();
		s.runserver();

	}

	public void runserver() throws IOException {
		Runnable run = new Connect();
		Thread RunThread = new Thread(run);
		RunThread.start();
	}

}
