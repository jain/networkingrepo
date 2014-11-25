import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class NetworkClientSetup {
	// multiply = 1
	// add = 0
	//private ArrayList<Node> packets;
	private HashMap<Integer, Node> packetMap;
	private LinkedList<Integer> queue;
	public void breakFile(byte[] data, int size) throws IOException{
		/*FileOutputStream out = new FileOutputStream("copy.pdf");
		out.write(data);
		out.close();*/
		int count = 0;
		int seqNum = 1;
		while (count<data.length){
			byte temp[] = new byte[size];
			for(int i = 0; i<size&&i+count<data.length; i++){
				temp[i] = data[i+count];
			}
			Node toSend = new Node(temp, seqNum);
			//packets.add(toSend);
			queue.addLast(seqNum);
			packetMap.put(seqNum, toSend);
			seqNum++;
			count+=size;
		}
		/*PrintWriter writer = new PrintWriter("copy.pdf");
		String s = "";
		for(Node n: packets){
			s+=n;
		}
		writer.write(s);
		writer.close();*/
	}
	public boolean ftpComplete(){
		return (packetMap.isEmpty());
	}
	public void recievedAck(int seqNum){
		if(packetMap.containsKey(seqNum)){
			packetMap.remove(seqNum);
			//return (packetMap.isEmpty());
			//	return true;
		}
		//return false;
	}
	public Node sendNextPacket(){
		if(!queue.isEmpty()) return packetMap.get(queue.removeFirst());//packets.get(index); 
		return null;
	}

	public NetworkClientSetup(String[] args) {
		//packets = new ArrayList<Node>();
		packetMap = new HashMap<Integer, Node>();
		queue = new LinkedList<Integer>();
		/*int command = -1, input1 = 0, input2 = 0, port = 0;
		String ip = "";
		try {
			if (args[1].toLowerCase().equals("multiply")) {
				command = 1;
			} else if (args[1].toLowerCase().equals("add")) {
				command = 0;
			} else {
				System.out.println("Invalid command");
				return;
			}
			input1 = Integer.parseInt(args[2]);
			input2 = Integer.parseInt(args[3]);
			if (!(input1 >= 0 && input1 <= 65535 && input2 >= 0 && input2 <= 65535)) {
				System.out.println("Invalid command");
				return;
			}
			ip = args[0].split(":")[0];
			port = Integer.parseInt(args[0].split(":")[1]);
			if (!(port >= 0 && port <= 65535)) {
				System.out.println("Invalid command");
				return;
			}
			if (!check(ip)) {
				System.out.println("Invalid command");
				return;
			}
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(ip);
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			sendData = (command + " " + input1 + " " + input2 + " ").getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
			clientSocket.setSoTimeout(2000);
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("From server: " + modifiedSentence);
			clientSocket.close();
		} catch (UnknownHostException e) {
			System.out.println("Invalid command");
		} catch (SocketTimeoutException e) {
			System.out.println("The server has not answered in 2 seconds.");
			System.out
					.println("Enter \"retry\" to resend the message or \"exit\" to exit the application:");
			Scanner scan = new Scanner(System.in);
			String input = scan.nextLine();
			if (input.toLowerCase().equals("exit")) {
				return;
			} else if (input.toLowerCase().equals("retry")) {
				boolean retry = true;
				while (retry)
					try {
						DatagramSocket clientSocket = new DatagramSocket();
						InetAddress IPAddress = InetAddress.getByName(ip);
						byte[] sendData = new byte[1024];
						byte[] receiveData = new byte[1024];
						sendData = (command + " " + input1 + " " + input2 + " ")
								.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(
								sendData, sendData.length, IPAddress, port);
						clientSocket.send(sendPacket);
						clientSocket.setSoTimeout(2000);
						DatagramPacket receivePacket = new DatagramPacket(
								receiveData, receiveData.length);
						clientSocket.receive(receivePacket);
						String modifiedSentence = new String(
								receivePacket.getData());
						System.out.println("From server: " + modifiedSentence);
						retry = false;
						clientSocket.close();
					} catch (SocketTimeoutException e1) {
						System.out.println("The server has not answered in 2 seconds.");
						System.out
								.println("Enter \"retry\" to resend the message or \"exit\" to exit the application:");
						scan = new Scanner(System.in);
						input = scan.nextLine();
						if (input.toLowerCase().equals("exit")) {
							retry = false;
							return;
						}else if (input.toLowerCase().equals("retry")) {
							retry = true;
						} else{
							System.out.println("Invalid command");
							retry = false;
							return;
						}
					} catch (Exception e1) {

					}
			} else {
				System.out.println("Invalid command");
			}
		} catch (SocketException e) {
			System.out.println("Invalid command");
		} catch (IOException e) {
			System.out.println("Invalid command");
		} catch (Exception e) {
			System.out.println("Invalid command");
		}*/
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
}