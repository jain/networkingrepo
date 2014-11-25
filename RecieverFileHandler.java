import java.util.HashMap;
import java.util.Map.Entry;


public class RecieverFileHandler {
	private HashMap<Integer, byte[]> packets;
	public RecieverFileHandler(){
		packets = new HashMap<Integer, byte[]>();
	}
	public boolean addData(byte[] data, int seqNum, short checkSum){
		if(!checkData(data, checkSum)) return false;
		if(packets.containsKey(seqNum)) return false;
		packets.put(seqNum, data);
		return true;
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
}
