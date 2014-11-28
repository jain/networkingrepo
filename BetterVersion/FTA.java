package Client;

public class FTA implements Runnable{
	private int myPort;
	private String ip;
	private int port;
	private int window;
	private int mode;
	private String fileName;
	private boolean connection;
	public FTA(String myPort, String ip, String port){
		// TODO Auto-generated constructor stub
		connection = false;
		mode = -1;
		window = Integer.MAX_VALUE;
		this.myPort = Integer.parseInt(myPort);
		if (!(this.myPort >= 0 && this.myPort <= 65535&&(this.myPort & 1) == 0)) {
			System.out.println("Invalid command");
			return;
		}
		this.port = Integer.parseInt(port);
		if (!(this.port >= 0 && this.port <= 65535)) {
			System.out.println("Invalid command");
			return;
		}
		this.ip = ip;
		if (!check(this.ip)) {
			System.out.println("Invalid command");
			return;
		}
	}
	private boolean check(String ip) {
		// TODO Auto-generated method stub
		try {
			String p1 = ip.split("\\.")[0];
			String p2 = ip.split("\\.")[1];
			String p3 = ip.split("\\.")[2];
			String p4 = ip.split("\\.")[3];
			if (!(p1 + "." + p2 + "." + p3 + "." + p4).equals(ip)) {
				return false;
			}
			int ip1 = Integer.parseInt(p1);
			int ip2 = Integer.parseInt(p2);
			int ip3 = Integer.parseInt(p3);
			int ip4 = Integer.parseInt(p4);
			if (!(ip1 >= 0 && ip1 <= 255)) {
				return false;
			}
			if (!(ip2 >= 0 && ip2 <= 255)) {
				return false;
			}
			if (!(ip3 >= 0 && ip3 <= 255)) {
				return false;
			}
			if (!(ip4 >= 0 && ip4 <= 255)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public void connect() {
		
		connection = true;
	}
	public void setWindow(int window) {
		// TODO Auto-generated method stub
		this.window = window;
	}
	public void setFileName(String string) {
		// TODO Auto-generated method stub
		fileName = string;
	}
	public void setMode(int i) {
		// TODO Auto-generated method stub
		mode = i;
	}
	public boolean isReady() {
		// TODO Auto-generated method stub
		return (mode!=-1&&connection);
	}
	@Override
	public void run(){
		// TODO Auto-generated method stub
		boolean execute = true;
		while(execute&&!Thread.interrupted()){

		}

	}
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}
}
