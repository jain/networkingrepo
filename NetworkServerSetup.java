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
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkServerSetup {
	static class TimerTaskMe extends TimerTask{
		Timer timer;
		public TimerTaskMe(Timer timer){
			super();
			this.timer = timer;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("#noob");
			timer.cancel();
		}
	}
	public static void main(String[] args) throws IOException {
		/*short source = 4343;
		byte[] bytes = ByteBuffer.allocate(2).putShort(source).array();
		for (byte b : bytes) {
			System.out.format("0x%x ", b);
		}
		ByteBuffer by = ByteBuffer.wrap(bytes);
		ShortBuffer a = by.asShortBuffer();
		System.out.println(a.get());*/
		contactServer();
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
	}
	/*Timer t = new Timer();
		TimerTaskMe task = new TimerTaskMe(t);
		t.schedule(task, 1000);
		System.out.println("noob");
		String name = "noob.asd";
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
	public static void contactServer() throws IOException {
		int s = 4343;
		// TODO Auto-generated method stub
		//String filename = "copy";
		String filename = "cracking_coding.pdf";
		//String filename = "noob.asd";
		Path path = Paths.get(filename);
		byte[] fileData = Files.readAllBytes(path);
		NetworkClientSetup ncs = new NetworkClientSetup(null);
		int size = ncs.breakFile(fileData, 1000);
		String input = size + "!copy"+ filename;// fix delimiter
		//input = checkSum(input) + input;
		//byte[] toSend = packet.array();
		byte[] toSend = createPacket(input.getBytes(), 0);
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] receiveData = new byte[1024];
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		Node node = ncs.sendNextPacket();
		do{
			toSend = createPacket(node.getData(), node.getSeqNum());
			sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, s);
			clientSocket.send(sendPacket);
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			ncs.recievedAck(node.getSeqNum());
			node = ncs.sendNextPacket();
		}while(!ncs.ftpComplete());
		clientSocket.close();
	}


	private static byte[] createPacket(byte[] input, int seqNo) {
		// TODO Auto-generated method stub
		short source = 4343;
		short dest = 3636;
		int seqNum = seqNo;
		byte synchronization = 1;
		byte finishConnection = 0;
		byte ack = 0;
		byte mode = 1;
		short length = (short)input.length;
		ByteBuffer packet = ByteBuffer.allocate(14+length);
		packet.putShort(source);
		packet.putShort(dest);
		packet.putInt(seqNum);
		packet.put(synchronization);
		packet.put(finishConnection);
		packet.put(ack);
		packet.put(mode);
		packet.putShort(length);
		packet.put(input);
		byte[] data = packet.array();
		short checkSum = checkSum(data);
		packet = ByteBuffer.allocate(16+length);
		packet.putShort(checkSum);
		packet.put(data);
		return packet.array();
	}

	private static short checkSum(byte[] data) {
		short checkSum = 0;
		byte A = 0;
		byte B = 0;
		for(int i = 0; i<data.length; i++){
			A+= data[i];
			B+= A;
		}
		checkSum = A;
		checkSum = (short) (checkSum<<8);
		checkSum+= B;
		return checkSum;
	}

}
