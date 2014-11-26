import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkClientSetup {
	private ConcurrentHashMap<Integer, Node> packetMap;
	private ConcurrentLinkedQueue<Integer> queue;
	private Set<Integer> seqNums;
	private Set<Integer> current;
	public int breakFile(byte[] data, int size) throws IOException{
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
		}
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