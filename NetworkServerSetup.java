import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class NetworkServerSetup {
	private static int current;
	private static int max;
	private static ConcurrentHashMap<Integer, Timer> map;
	private static NetworkClientSetup ncs;
	private static DatagramSocket clientSocket;
	public static void main(String[] args) throws IOException {
		map = new ConcurrentHashMap<Integer, Timer>();
		max = 1;
		current = 0;
		try{
			contactServer();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	static class TimerTaskMe extends TimerTask{
		private Timer timer;
		private Node node;
		public TimerTaskMe(Timer timer, Node node){
			super();
			this.timer = timer;
			this.node = node;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//System.out.println("#noob");
			//timer.cancel();
			//System.out.println("yolo");
			//timer.
			/*timer = new Timer();
			timer.schedule(this, 1000);*/
			// add node to end of queue
			// if being transferred right now == 0 resend
			/*if(ncs.containsSeqNum(node.getSeqNum())){
				if(map.size()==0){
					try{
						sendMore();
					} catch (IOException e){

					}
				}
				return;
			}*/

			timer.cancel();
			map.remove(node.getSeqNum());
			ncs.removeCurrent(node.getSeqNum());
			current = 0;
			max--;
			ncs.addPacket(node.getSeqNum());
			//ncs.addToEnd(node);
			if(map.size()==0){
				max = 1;
				/*try {
					//restart();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				try{
					sendMore();
				} catch (IOException e){

				}
			}
		}
		public void sendMore() throws IOException{
			//System.out.println(node.getSeqNum());
			//System.out.println(ncs.ftpComplete());
			/*int s = 8000;
			Node node = ncs.sendNextPacket();
			System.out.println("prob is here");
			if(node==null) return;
			if(ncs.containsCurrent(node.getSeqNum())) return;
			InetAddress IPAddress = InetAddress.getByName("localhost");
			byte[] toSend = node.getPacket();
			DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
			Timer time = new Timer();
			TimerTaskMe tsk = new TimerTaskMe(time, node);
			time.schedule(tsk, 2000);
			map.put(node.getSeqNum(), time);
			clientSocket.send(sendPacket);*/
			sendNextPacket();
		}
	}
	static class TimerTaskMe2 extends TimerTask{
		private Timer timer;
		public TimerTaskMe2(Timer timer){
			super();
			this.timer = timer;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//System.out.println("#noob");
			//timer.cancel();
			//System.out.println("yolo");
			//timer.
			timer.cancel();
			try {
				contactServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public static void contactServer() throws IOException {
		int s = 8000;
		// TODO Auto-generated method stub
		//String filename = "copy";
		String filename = "cracking_coding.pdf";
		//String filename = "noob.asd";
		Path path = Paths.get(filename);
		byte[] fileData = Files.readAllBytes(path);
		ncs = new NetworkClientSetup(null);
		int size = ncs.breakFile(fileData, 1000);
		String input = size + "!copy"+ filename;// fix delimiter
		//input = checkSum(input) + input;
		//byte[] toSend = packet.array();
		//byte[] toSend = createPacket(input.getBytes(), 0);
		Node first = new Node(input.getBytes(), 0);
		byte[] toSend = first.getPacket();
		clientSocket = new DatagramSocket(4000);
		Timer t = new Timer();
		TimerTaskMe2 task = new TimerTaskMe2(t);
		t.schedule(task, 1000);
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] receiveData = new byte[1024];
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		t.cancel();	
		Thread thread = new Thread(new CheckClientReception(ncs, clientSocket));
		thread.start();
		sendNextPacket();
		//clientSocket.close();
		//restart();
		/*Node node = ncs.sendNextPacket();
		do{
			if(node==null) continue;
			System.out.println(node.getSeqNum());
			toSend = node.getPacket();
			sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
			Timer time = new Timer();
			TimerTaskMe tsk = new TimerTaskMe(time, node);
			time.schedule(tsk, 2000);
			map.put(node.getSeqNum(), time);
			clientSocket.send(sendPacket);
			receiveData = new byte[1024];
			here:{
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				ByteBuffer gotten = ByteBuffer.wrap(receivePacket.getData());
				if(!checkData(receiveData, gotten.getShort(0))){
					break here;
				}
				if (receivePacket.getData()[12]==1&&map.containsKey(gotten.getInt(6))){
					Timer tmp = map.remove(gotten.getInt(6)); // can be error if not in map...
					tmp.cancel();
					ncs.recievedAck(gotten.getInt(6));
					//map.get(gotten.getInt(6)).cancel(); /
					current++;
					if(current==max){
						max++;
						current = 0;
					}
					node = ncs.sendNextPacket();
				}
			}
		}while(!ncs.ftpComplete());*/
	}
	public static boolean checkData(byte[] data, short sum){
		short checkSum = 0;
		byte A = 0;
		byte B = 0;
		for(int i = 2; i<data.length; i++){
			A+= data[i];
			B+= A;
		}
		checkSum = A;
		checkSum = (short) (checkSum<<8);
		checkSum+= B;
		return (checkSum==sum);
	}
	public static void handle(DatagramPacket receivePacket) throws IOException {
		// TODO Auto-generated method stub
		ByteBuffer gotten = ByteBuffer.wrap(receivePacket.getData());
		if(!checkData(receivePacket.getData(), gotten.getShort(0))){
			return;
		}
		if (receivePacket.getData()[12]==1&&map.containsKey(gotten.getInt(6))){
			Timer tmp = map.remove(gotten.getInt(6)); // can be error if not in map...
			tmp.cancel();
			ncs.recievedAck(gotten.getInt(6));
			//map.get(gotten.getInt(6)).cancel(); /
			current++;
			if(current==max){
				max++;
				current = 0;
			}
			sendNextPacket();
		}
	}
	private static void sendNextPacket() throws IOException {
		// TODO Auto-generated method stub
		int s = 8000;
		InetAddress IPAddress = InetAddress.getByName("localhost");
		Node node = ncs.sendNextPacket();
		//System.out.println(node.getSeqNum());	
		if(node==null) return;
		byte[] toSend = node.getPacket();
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
		System.out.println(node.getSeqNum());
		Timer time = new Timer();
		TimerTaskMe tsk = new TimerTaskMe(time, node);
		time.schedule(tsk, 2000);
		map.put(node.getSeqNum(), time);
		clientSocket.send(sendPacket);
	}
}
