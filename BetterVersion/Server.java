import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable{
	private short myPort;
	private String ip;
	private short port;
	private int window;
	private boolean possible;
	private DatagramSocket serverSocket;
	private InetAddress IPAddress;
	private byte mode;
	FTPRecieve ftprec;
	FTPSend ftpsend;
	boolean mood1;
	int max;
	int current;
	private ConcurrentHashMap<Integer, Timer> map;
	public Server(String myPort, String ip, String port) throws SocketException, UnknownHostException{
		// TODO Auto-generated constructor stub
		max = 1;
		current = 0;
		map = new ConcurrentHashMap<Integer, Timer>();
		mood1 = false;
		ftpsend = null;
		ftprec = null;
		mode = -1;
		window = Integer.MAX_VALUE;
		this.myPort = Short.parseShort(myPort);
		if (!(this.myPort >= 0 && this.myPort <= 65535&&(this.myPort & 1) == 1)) {
			System.out.println("Invalid command");
			possible = false;
			return;
		}
		this.port = Short.parseShort(port);
		if (!(this.port >= 0 && this.port <= 65535)) {
			System.out.println("Invalid command");
			possible = false;
			return;
		}
		this.ip = ip;
		if (!check(this.ip)) {
			System.out.println("Invalid command");
			possible = false;
			return;
		}
		possible = true;
		serverSocket = new DatagramSocket(this.myPort);
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
	public void setWindow(int win){
		window = win;
		//System.out.println(win);
	}
	@Override
	public void run(){
		// TODO Auto-generated method stub
		while(!Thread.interrupted()){
			try{
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				Packet packet = new Packet(receiveData);
				if(!packet.checkData(receiveData, packet.checkSum, packet.length+20)) continue;
				//System.out.println(packet.seqNum);
				if(packet.synchronization==1&&packet.mode==-1) acceptConnection(packet);
				else if(packet.synchronization==1) changeMode(packet);
				else if(packet.length>0){
					if(mode==1){
						ftprec.addData(packet.data, packet.seqNum);
						Packet send = new Packet(null,myPort, port,packet.seqNum,(byte)0,(byte)0,(byte)1, mode, window);
						byte[] toSend = send.getPacket();
						DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
						serverSocket.send(sendPacket);
					}
				}else if(mode==0){
					receiveData = new byte[1024];
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					try {
						serverSocket.receive(receivePacket);
						handle(receivePacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (IOException e){

			}
		}
	}
	public void sendNextPacket() throws IOException {
		byte[] data = ftpsend.sendNextPacket();
		if(data==null) return;
		//Packet packet = new Packet(input, source, dest, seqNum, synchronisation, finishConnection, ack, mode, window)
		Packet packet = new Packet(data, myPort, port, ftpsend.numToData.get(data),
				(byte)0, (byte)0, (byte)0, mode, window);
		byte[] toSend = packet.getPacket();
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
		Timer time = new Timer();
		TimerTaskSend tsk = new TimerTaskSend(time, data);
		time.schedule(tsk, 1000);
		map.put(ftpsend.numToData.get(data), time);
		serverSocket.send(sendPacket);
	}
	private void handle(DatagramPacket receivePacket) throws IOException {
		// TODO Auto-generated method stub
		Packet packet = new Packet(receivePacket.getData());
		if(!packet.checkData(packet.getPacket(), packet.checkSum, packet.length+20)) return;
		if (receivePacket.getData()[12]==1&&map.containsKey(packet.seqNum)){
			Timer tmp = map.remove(packet.seqNum); // can be error if not in map...
			tmp.cancel();
			ftpsend.recievedAck(packet.seqNum);
			//map.get(gotten.getInt(6)).cancel(); /
			current++;
			if(current==max){
				max++;
				if(max>window){
					max = window;
				}
				current = 0;
				sendNextPacket(); // conjestion control
			}
			sendNextPacket();
		}
	}
	private void changeMode(Packet packet) throws IOException {
		// TODO Auto-generated method stub
		mode = packet.mode;
		int seqNum = packet.seqNum;
		//Packet packet = new Packet(data,myPort, port,seqNum,(byte)0,(byte)0,(byte)0,mode, window);
		if(mode==1){
			if(ftprec==null){
				ftprec = new FTPRecieve(new String(packet.data));
				ftprec.setNumOfPackets(packet.seqNum*-1);
			}
			Packet send = new Packet(null,myPort, port,seqNum,(byte)0,(byte)0,(byte)1, mode, window);
			byte[] toSend = send.getPacket();
			DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}else{
			ftpsend = new FTPSend(new String(packet.data));
			seqNum = -1*ftpsend.getNumOfPackets();
			packet = new Packet(ftpsend.getFile(),myPort, port,seqNum,(byte)1,(byte)0,(byte)0,mode, window);

			byte[] toSend = packet.getPacket();
			byte[] receiveData = new byte[1024];
			// has to keep sending like client

			Timer timeOut = new Timer();
			TimerTaskMode task = new TimerTaskMode(timeOut, packet);
			timeOut.schedule(task, 1000);
			DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
			serverSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			//System.out.println(3);
			packet = new Packet(receiveData);
			if(packet.checkData(receiveData, packet.checkSum, packet.length+20)){
				if(packet.ack==1&&packet.mode==mode&&packet.seqNum==seqNum){
					//System.out.println(4);
					mood1 = true;
					timeOut.cancel();
					sendNextPacket();
				}
			}
		}
	}
	private void acceptConnection(Packet packet) throws IOException {
		//Packet packet = new Packet(input, source, dest, seqNum, synchronisation, finishConnection, ack, mode, window)

		Packet send = new Packet(null,myPort, port,-1,(byte)1,(byte)0,(byte)1,packet.mode, window);
		byte[] toSend = send.getPacket();
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
		serverSocket.send(sendPacket);
		//System.out.println("rec");
	}
	public boolean isPossible() {
		return possible;
	}
	class TimerTaskMode extends TimerTask{
		private Timer timer;
		private Packet packet;
		public TimerTaskMode(Timer timer, Packet packet){
			super();
			this.timer = timer;
			this.packet = packet;
		}
		@Override
		public void run() {
			//System.out.println("hello1");
			timer.cancel();
			try {
				if (mood1 == false) changeMode(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	class TimerTaskSend extends TimerTask{
		private Timer timer;
		private byte[] data;
		public TimerTaskSend(Timer timer, byte[] data){
			super();
			this.timer = timer;
			this.data= data;
		}
		@Override
		public void run() {
			//System.out.println("hello2");
			int seqNum = ftpsend.numToData.get(data);
			timer.cancel();
			map.remove(seqNum);
			ftpsend.removeCurrent(seqNum);
			current = 0;
			max--;
			if(max==0){
				max = 1;
			}
			ftpsend.addPacket(seqNum);
			//ncs.addToEnd(node);
			if(map.size()==0){
				try{
					sendNextPacket();
				} catch (IOException e){

				}
			}
		}
	}
}
