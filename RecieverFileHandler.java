import java.util.HashMap;
import java.util.Map.Entry;


public class RecieverFileHandler {
	private HashMap<Integer, String> packets;
	public RecieverFileHandler(){
		packets = new HashMap<Integer, String>();
	}
	public boolean addPacket(String data, int seqNum){
		if(packets.containsKey(seqNum)) return false;
		packets.put(seqNum, data);
		return true;
	}
	public String reOrder(){
		String output = "";
		String arr[] = new String[packets.size()];
		for(Entry<Integer, String> entry : packets.entrySet()){
			arr[entry.getKey()-1] = entry.getValue();
		}
		for(String data: arr){
			output+=data;
		}
		return output;
	}
	public boolean checkData(byte[] data, short sum){
		short checkSum = 0;
		byte A = 0;
		byte B = 0;
		for(int i = 0; i<data.length; i++){
			A+= data[i];
			B+= A;
		}
		checkSum = (short)(A<<8);
		checkSum+= B;
		return (checkSum == sum);
	}
	public boolean addData(String data, int seqNum){
		if(packets.containsKey(seqNum)) return false;
		packets.put(seqNum, data);
		return true;
	}
}
