import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FTPSend {
	ConcurrentHashMap<Integer, byte[]> packetMap;
	ConcurrentLinkedDeque<Integer> queue;
	ConcurrentHashMap<byte[], Integer> numToData;
	Set<Integer> seqNums;
	Set<Integer> current;
	String fileName;
	int segments;
	public FTPSend(String filename) throws IOException {
		// TODO Auto-generated constructor stub
		numToData = new ConcurrentHashMap<byte[], Integer>();
		packetMap = new ConcurrentHashMap<Integer, byte[]>();
		queue = new ConcurrentLinkedDeque<Integer>();
		seqNums = Collections.synchronizedSet(new HashSet<Integer>());
		current = Collections.synchronizedSet(new HashSet<Integer>());
		fileName = filename;
		Path path = Paths.get(filename);
		byte[] fileData = Files.readAllBytes(path);
		segments = breakFile(fileData, 1000);
	}
	public int getNumOfPackets(){
		return segments;
	}
	public int breakFile(byte[] data, int size) throws IOException{
		int count = 0;
		int seqNum = 1;
		while (count<data.length){
			byte temp[] = new byte[size];
			for(int i = 0; i<size&&i+count<data.length; i++){
				temp[i] = data[i+count];
			}
			queue.addLast(seqNum);
			packetMap.put(seqNum, temp);
			numToData.put(temp, seqNum);
			seqNum++;
			count+=size;
		}
		return (seqNum-1);
	}
	public byte[] getFile() {
		// TODO Auto-generated method stub
		return fileName.getBytes();
	}
	public byte[] sendNextPacket() {
		// TODO Auto-generated method stub
		return null;
	}

}
