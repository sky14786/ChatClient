package application;

public class Model {
	private String nickname = new String();
	private String ip = new String();
	private String port = new String();

	public void SetNickName(String nick) {
		nickname = nick;
	}

	public String GetNickNmae() {
		return nickname;
	}

	public void SetSocket(String ip, String port) {
		this.ip = ip;
		this.port = port;

	}

	public String GetIP() {
		return ip;
	}

	public String GetPort() {
		return port;
	}

}
