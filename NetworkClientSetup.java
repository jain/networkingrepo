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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkClientSetup {
	// multiply = 1
	// add = 0
	//private ArrayList<Node> packets;
	private ConcurrentHashMap<Integer, Node> packetMap;
	private ConcurrentLinkedQueue<Integer> queue;
	private Set<Integer> seqNums;
	private Set<Integer> current;
	public int breakFile(byte[] data, int size) throws IOException{
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
			queue.add(seqNum);
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
		return (seqNum-1);
	}
	public boolean ftpComplete(){
		return (packetMap.isEmpty());
	}
	public void recievedAck(int seqNum){
		current.remove(seqNum);
		seqNums.add(seqNum);
		System.out.println("recieved" + seqNums.size());
		queue.remove(seqNum); // tmp
		if(packetMap.containsKey(seqNum)){
			packetMap.remove(seqNum);
			//return (packetMap.isEmpty());
			//	return true;
		}
		//return false;
	}
	public Node sendNextPacket(){
		if(!queue.isEmpty()) {
			int seqNo = queue.peek();
			System.out.println(seqNo +"," +current.contains(seqNo));
			if(current.contains(seqNo)) return null;
			System.out.println(seqNo);
			current.add(seqNo);
			queue.remove();
			return packetMap.get(seqNo);//packets.get(index); 
		}
		return null;
	}
	public void addPacket(int seqNum){
		queue.add(seqNum);
	}
	public boolean containsSeqNum(Integer num){
		return seqNums.contains(num);
	}
	public NetworkClientSetup(String[] args) {
		//packets = new ArrayList<Node>();
		packetMap = new ConcurrentHashMap<Integer, Node>();
		queue = new ConcurrentLinkedQueue<Integer>();
		seqNums = Collections.synchronizedSet(new HashSet<Integer>());
		current = Collections.synchronizedSet(new HashSet<Integer>());
	}
	public boolean containsCurrent(Integer num){
		return (current.contains(num));
	}
	public void removeCurrent(int seqNum) {
		// TODO Auto-generated method stub
		current.remove(seqNum);
	}
}