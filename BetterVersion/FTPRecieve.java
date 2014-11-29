import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class FTPRecieve {
	private String fileName;
	private int numOfPackets;
	private HashMap<Integer, byte[]> packets;
	private long start;
	public FTPRecieve(String filename){
		start = System.currentTimeMillis();
		fileName = filename;
		packets = new HashMap<Integer, byte[]>();
	}
	public boolean addData(byte[] data, int seqNum){
		if(packets.containsKey(seqNum)) return false;
		packets.put(seqNum, data);
		numOfPackets--;
		//System.out.println("remaining" +  numOfPackets);
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
		long stop = System.currentTimeMillis();
		double time = ((double)(stop-start))/1000.0;
		System.out.println("throughput="+(time/((double)output.length+numOfPackets*20)));
		FileOutputStream fos = new FileOutputStream("copy" + fileName);
		fos.write(output);
		fos.close();
	}
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
	public byte[] getFile() {
		// TODO Auto-generated method stub
		return fileName.getBytes();
	}
	public int getNumOfPackets() {
		return numOfPackets;
	}
	public void setNumOfPackets(int numOfPackets) {
		this.numOfPackets = numOfPackets;
	}
}
