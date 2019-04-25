package application;

public class Model {
	private String nickName = new String();
	private String ip = new String();
	private String port = new String();

	public void setNickName(String nick) {
		nickName = nick;
	}

	public String getNickNmae() {
		return nickName;
	}

	public void setSocket(String ip, String port) {
		this.ip = ip;
		this.port = port;

	}

	public String getIP() {
		return ip;
	}

	public String getPort() {
		return port;
	}

}
