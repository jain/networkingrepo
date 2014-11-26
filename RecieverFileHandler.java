import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;


public class RecieverFileHandler {
	private HashMap<Integer, byte[]> packets;
	private String filename;
	private int numOfPackets;
	public RecieverFileHandler(){
		packets = new HashMap<Integer, byte[]>();
	}
	public RecieverFileHandler(String name, int numOfPackets){
		packets = new HashMap<Integer, byte[]>();
		this.numOfPackets=numOfPackets;
		filename = name;
	}
	public boolean addData(byte[] data, int seqNum){
		if(packets.containsKey(seqNum)) return false;
		packets.put(seqNum, data);
		numOfPackets--;
		System.out.println("remaining" +  numOfPackets);
		if(numOfPackets==0){
			try {
				createFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	private void createFile() throws IOException {
		// TODO Auto-generated method stub
		byte[] output = reOrder();
		FileOutputStream fos = new FileOutputStream("copy" + filename);
		fos.write(output);
		fos.close();
	}
	/*public boolean addPacket(byte data[], int seqNum){
		if(packets.containsKey(seqNum)) return false;
		packets.put(seqNum, data);
		return true;
	}*/
	public byte[] reOrder(){
		byte[] arr[] = new byte[packets.size()][];
		int len = 0;
		for(Entry<Integer, byte[]> entry : packets.entrySet()){
			arr[entry.getKey()-1] = entry.getValue();
			len+=entry.getValue().length;
		}
		byte[] output = new byte[len];
		int counter = 0;
		for(byte[] data: arr){
			for(byte bit : data){
				output[counter] = bit;
				counter++;
			}
		}
		return output;
	}
}
