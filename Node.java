import java.nio.ByteBuffer;


public class Node {
	private byte[] packet;
	private int seqNum;
	//private int seqNum;
	//private short checkSum;
	public Node(byte[] data, int seqNum){
		//this.setData(data);
		//this.setSeqNum(seqNum);
		/*checkSum = 0;
		byte A = 0;
		byte B = 0;
		for(int i = 0; i<data.length; i++){
			A+= data[i];
			B+= A;
		}
		checkSum = A;
		checkSum = (short) (checkSum<<8);
		checkSum+= B;*/
		this.seqNum = seqNum;
		packet = createPacket(data, seqNum);
	}
	public int getSeqNum(){
		return seqNum;
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

	/*public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}*/
	public byte[] getPacket() {
		return packet;
	}
	public void setData(byte[] packet) {
		this.packet = packet;
	}
	/*public short getCheckSum() {
		return checkSum;
	}
	@Override
	public String toString(){
		//return (new String(data) + "seq Num:" + seqNum + "checkSum:" + checkSum);
		return (new String(data));
	}*/
}