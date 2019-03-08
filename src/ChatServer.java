

public class ChatServer {
	private static ConnectThread connectthread;
	private static ReceiveThread receivethread;

	public static void main(String[] args) {
		connectthread = new ConnectThread();
		receivethread = new ReceiveThread();
		connectthread.run();
//		receivethread.run();
	}

}


class ConnectThread extends Thread {
	private Manager manager;

	ConnectThread() {
		manager = new Manager();
	}

	public void run() {
		manager.PhysicalConnect();
	}
}

class ReceiveThread extends Thread{
	private Manager manager;
	
	ReceiveThread(){
		manager = new Manager();
	}
	public void run() {
		manager.ReceiveMessage();
	}
}
