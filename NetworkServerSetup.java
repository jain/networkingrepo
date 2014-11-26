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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class NetworkServerSetup {
	private static int current;
	private static int max;
	private static ConcurrentHashMap<Integer, Timer> map;
	private static NetworkClientSetup ncs;
	public static void main(String[] args) throws IOException {
		map = new ConcurrentHashMap<Integer, Timer>();
		max = 1;
		current = 0;
		try{
			contactServer();
		} catch (IOException e){

		}
	}
	static class TimerTaskMe extends TimerTask{
		private Timer timer;
		private Node node;
		private DatagramSocket clientSocket;
		public TimerTaskMe(Timer timer, Node node, DatagramSocket clientSocket){
			super();
			this.timer = timer;
			this.node = node;
			this.clientSocket = clientSocket;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//System.out.println("#noob");
			//timer.cancel();
			//System.out.println("yolo");
			//timer.
			timer.cancel();
			/*timer = new Timer();
			timer.schedule(this, 1000);*/
			// add node to end of queue
			// if being transferred right now == 0 resend
			map.remove(node.getSeqNum());
			current = 0;
			max--;
			//ncs.addToEnd(node);
			if(map.size()==0){
				max = 1;
				clientSocket.close();
				try {
					restart();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	static class TimerTaskMe2 extends TimerTask{
		private Timer timer;
		private DatagramSocket clientSocket;
		public TimerTaskMe2(Timer timer, DatagramSocket clientSocket){
			super();
			this.timer = timer;
			this.clientSocket = clientSocket;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//System.out.println("#noob");
			//timer.cancel();
			//System.out.println("yolo");
			//timer.
			timer.cancel();
			clientSocket.close();
			try {
				contactServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*short source = 4343;
		byte[] bytes = ByteBuffer.allocate(2).putShort(source).array();
		for (byte b : bytes) {
			System.out.format("0x%x ", b);
		}
		ByteBuffer by = ByteBuffer.wrap(bytes);
		ShortBuffer a = by.asShortBuffer();
		System.out.println(a.get());*/
	//contactServer();
	/*String filename = "cracking_coding.pdf";
		//String filename = "noob.asd";
		Path path = Paths.get(filename);
		byte[] data = Files.readAllBytes(path);
		NetworkClientSetup ncs = new NetworkClientSetup(null);
		ncs.breakFile(data, 1000);
		Node node = ncs.sendNextPacket();
		while(!ncs.ftpComplete()){
			rfh.addData(node.getData(), node.getSeqNum(), node.getCheckSum());
			rfh.addData(node.getData(), node.getSeqNum(), node.getCheckSum());
			ncs.recievedAck(node.getSeqNum());
			ncs.recievedAck(node.getSeqNum());
			node = ncs.sendNextPacket();
		}
		//System.out.println(""+ncs.recievedAck(0));
		System.out.println(ncs.sendNextPacket());
		byte[] output = rfh.reOrder();
		FileOutputStream fos = new FileOutputStream("copy" + filename);
		fos.write(output);
		fos.close();*/
	//}
	/*Timer tim = new Timer();
		TimerTaskMe task = new TimerTaskMe(tim, null);
		tim.scheduleAtFixedRate(task, 1000, 1000);*/
	//}
	//t.schedule(task, 1000);
	//System.out.println("noob");
	/*String name = "noob.asd";
		Path path = Paths.get(name);
		byte[] data = Files.readAllBytes(path);
		FileOutputStream fos = new FileOutputStream("copy" + name);
		fos.write(data);
		fos.close();

	 */
	//t.cancel();
	/*FileInputStream fs = new FileInputStream("cracking_coding.pdf");
		FileOutputStream os = new FileOutputStream("copy.pdf");
		int b;
		while ((b = fs.read()) != -1) {
		    os.write(b);
		}
		os.close();
		fs.close();*/
	/*File file = new File("cracking_coding.pdf");
		FileReader reader = new FileReader(file);
		char[] chars = new char[(int) file.length()];
		reader.read(chars);
		String content = new String(chars);
		reader.close();
		NetworkClientSetup ncs = new NetworkClientSetup(null);
		ncs.breakFile(content, 1000);
		PrintWriter writer = new PrintWriter("copy.pdf");
		writer.println(content);
		writer.close();*/
	/*try {
			int port = Integer.parseInt(args[0]);
			if (!(port >= 0 && port <= 65535)) {
				System.out.println("Invalid command");
				return;
			}
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());
				int command = Integer.parseInt(sentence.split(" ")[0]);
				int input1 = Integer.parseInt(sentence.split(" ")[1]);
				int input2 = Integer.parseInt(sentence.split(" ")[2]);
				String output = "Cannot compute";
				if (command == 0) {
					if (input1 + input2 < 65536) {
						output = (input1 + input2) + "";
					}
				} else {
					if (input1 * input2 < 65536) {
						output = (input1 * input2) + "";
					}
				}
				InetAddress IPAddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				sendData = output.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
		} catch (SocketException e) {
			System.out.println("Invalid command");
		} catch (IOException e) {
			System.out.println("Invalid command");
		} catch (Exception e){
			System.out.println("Invalid command");
		}*/

	// 0 is recieve
	// 1 is upload
	private static void restart() throws IOException{
		// TODO Auto-generated method stub
		int s = 4343;
		Node node = ncs.sendNextPacket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		DatagramSocket clientSocket = new DatagramSocket(4342);
		do{
			System.out.println(node.getSeqNum());
			byte[] toSend = node.getPacket();
			DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
			Timer time = new Timer();
			TimerTaskMe tsk = new TimerTaskMe(time, node, clientSocket);
			time.schedule(tsk, 2000);
			map.put(node.getSeqNum(), time);
			clientSocket.send(sendPacket);
			byte[] receiveData = new byte[1024];
			here:{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				ByteBuffer gotten = ByteBuffer.wrap(receivePacket.getData());
				if(!checkData(receiveData, gotten.getShort(0))){
					break here;
				}
				if (receivePacket.getData()[12]==1){
					ncs.recievedAck(gotten.getInt(6));
					//map.get(gotten.getInt(6)).cancel(); /
					Timer tmp = map.remove(gotten.getInt(6)); // can be error if not in map...
					current++;
					if(current==max){
						max++;
						current = 0;
					}
					tmp.cancel();
					node = ncs.sendNextPacket();
				}
			}
		}while(!ncs.ftpComplete());
		clientSocket.close();
	}
	public static void contactServer() throws IOException {
		int s = 4343;
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
		DatagramSocket clientSocket = new DatagramSocket(4342);
		Timer t = new Timer();
		TimerTaskMe2 task = new TimerTaskMe2(t, clientSocket);
		t.schedule(task, 1000);
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] receiveData = new byte[1024];
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		t.cancel();
		/*Node node = ncs.sendNextPacket();
			do{

				//toSend = createPacket(node.getData(), node.getSeqNum());
				toSend = node.getPacket();
				sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
				Timer time = new Timer();
				TimerTaskMe tsk = new TimerTaskMe(time, node, clientSocket);
				time.schedule(tsk, 2000);
				map.put(node.getSeqNum(), time);
				clientSocket.send(sendPacket);
				here:{
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					ByteBuffer gotten = ByteBuffer.wrap(receivePacket.getData());
					if(!checkData(receiveData, gotten.getShort(0))){
						break here;
					}
					if (receivePacket.getData()[12]==1){
						ncs.recievedAck(gotten.getInt(6));
						//map.get(gotten.getInt(6)).cancel(); /
						Timer tmp = map.remove(gotten.getInt(6)); // can be error if not in map...
						tmp.cancel();
						node = ncs.sendNextPacket();
					}
				}
			}while(!ncs.ftpComplete());*/
		clientSocket.close();
		restart();
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
}