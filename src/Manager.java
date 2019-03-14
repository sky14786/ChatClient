import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

class Manager {
	private Boolean isduplicate;
	private ArrayList<Socket> clients = new ArrayList<>();
	private HashMap<Socket, String> hm = new HashMap<>();
	private ArrayList<String> nicknames = new ArrayList<>();
//	private ArrayList<DataInputStream> inputs = new ArrayList<>();
//	private ArrayList<DataOutputStream> outputs = new ArrayList<>();

	public void PutHashValue(Socket soc, String nick) {
		hm.put(soc, nick);
	}

	public void AddSocketList(Socket soc) {
		clients.add(soc);
	}

	public void AddNickNames(String nick) {
		nicknames.add(nick);
	}

	public HashMap<Socket, String> GetHashMap() {
		return hm;
	}

	public ArrayList<Socket> GetSocketList() {
		return clients;
	}

	public ArrayList<String> GetNickNames() {
		return nicknames;
	}

	public Boolean DuplicateCheck(String nickname) {
		isduplicate = false;
		for (int i = 0; i < nicknames.size(); i++) {
			if (nicknames.get(i).equals(nickname)) {
				isduplicate = true;
				return isduplicate;
			} else {
				isduplicate = false;
			}
		}
		return isduplicate;
	}

}
