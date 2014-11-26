
public class Node {
	private byte[] data;
	private int seqNum;
	private short checkSum;
	public Node(byte[] data, int seqNum){
		this.setData(data);
		this.setSeqNum(seqNum);
		checkSum = 0;
		byte A = 0;
		byte B = 0;
		for(int i = 0; i<data.length; i++){
			A+= data[i];
			B+= A;
		}
		checkSum = A;
		checkSum = (short) (checkSum<<8);
		checkSum+= B;
	}
	public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public short getCheckSum() {
		return checkSum;
	}
	@Override
	public String toString(){
		//return (new String(data) + "seq Num:" + seqNum + "checkSum:" + checkSum);
		return (new String(data));
	}
}