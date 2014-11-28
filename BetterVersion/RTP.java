package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class RTP implements Runnable{
	private short myPort;
	private String ip;
	private short port;
	private int window;
	private byte mode;
	private boolean connection;
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
	public RTP(String myPort, String ip, String port) throws SocketException, UnknownHostException{
		// TODO Auto-generated constructor stub
		connection = false;
		mode = -1;
		window = Integer.MAX_VALUE;
		this.myPort = Short.parseShort(myPort);
		if (!(this.myPort >= 0 && this.myPort <= 65535&&(this.myPort & 1) == 0)) {
			System.out.println("Invalid command");
			return;
		}
		this.port = Short.parseShort(port);
		if (!(this.port >= 0 && this.port <= 65535)) {
			System.out.println("Invalid command");
			return;
		}
		this.ip = ip;
		if (!check(this.ip)) {
			System.out.println("Invalid command");
			return;
		}
		clientSocket = new DatagramSocket(this.myPort);
		IPAddress = InetAddress.getByName("localhost");
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
	public void connect() throws IOException {
		Packet packet = new Packet(null,myPort, port,(byte)-1,(byte)1,(byte)0,(byte)0,(byte)-1, window);
		byte[] toSend = packet.getPacket();
		byte[] receiveData = new byte[1024];
		Timer timeOut = new Timer();
		TimerTaskConnect task = new TimerTaskConnect(timeOut);
		timeOut.schedule(task, 1000);
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		packet = new Packet(receiveData);
		if(packet.checkData(receiveData, packet.checkSum)){
			if(packet.synchronization==1&&packet.ack==1){
				timeOut.cancel();
				connection = true;
			}
		}
	}
	public void setWindow(int window) {
		// TODO Auto-generated method stub
		this.window = window;
	}
	public void setMode(int i) {
		// TODO Auto-generated method stub
		mode = (byte) i;
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
	class TimerTaskConnect extends TimerTask{
		private Timer timer;
		public TimerTaskConnect(Timer timer){
			super();
			this.timer = timer;
		}
		@Override
		public void run() {
			timer.cancel();
			try {
				connect();
				setServerMode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void setServerMode() {
			// TODO Auto-generated method stub
			
		}
	}
}
