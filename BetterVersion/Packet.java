import java.nio.ByteBuffer;

public class Packet {
	short source;
	short dest;
	ByteBuffer packet;
	int seqNum;
	byte synchronization;
	byte finishConnection;
	byte ack;
	byte mode;
	int window;
	short checkSum;
	byte[] data;
	short length;
	public Packet(byte[] receiveData){
		ByteBuffer gotten = ByteBuffer.wrap(receiveData);
		checkSum = gotten.getShort(0);
		source = gotten.getShort(2);
		dest = gotten.getShort(4);
		seqNum = gotten.getInt(6);
		synchronization = gotten.get(10);
		finishConnection = gotten.get(11);
		ack = gotten.get(12);
		mode = gotten.get(13);
		window = gotten.getInt(14);
		short dataLength = gotten.getShort(18);
		if(dataLength>0){
			byte[] data = new byte[dataLength];
			for(int i = 20; i<(20+dataLength); i++){
				data[i-20] = receiveData[i];
			}
			/*if(rfh==null&&gotten.getInt(6)==0){
				String reception = new String(data);
				System.out.println(reception);
				String[] fileData = reception.split("!");
				rfh = new RecieverFileHandler(fileData[1], Integer.parseInt(fileData[0]));
			}else{
				rfh.addData(data, gotten.getInt(6));
			}*/
			//System.out.println("name:" + new String(name));
		}
	}
	public Packet(byte[] input, short source, short dest, int seqNum, byte synchronisation, byte finishConnection, byte ack, byte mode, int window){
		this.source = source;
		this.dest = dest;
		this.seqNum = seqNum;
		synchronization = synchronisation;
		this.finishConnection = finishConnection;
		this.ack = ack;
		this.mode = mode;
		this.window = window;
		if(input!=null){
			length = (short)input.length;
			packet = ByteBuffer.allocate(18+length);
			packet.putShort(source);
			packet.putShort(dest);
			packet.putInt(seqNum);
			packet.put(synchronization);
			packet.put(finishConnection);
			packet.put(ack);
			packet.put(mode);
			packet.putInt(window);
			packet.putShort(length);
			packet.put(input);
			byte[] data = packet.array();
			checkSum = checkSum(data);
			packet = ByteBuffer.allocate(20+length);
			packet.putShort(checkSum);
			packet.put(data);
		}else{
			length = 0;
			packet = ByteBuffer.allocate(18);
			packet.putShort(source);
			packet.putShort(dest);
			packet.putInt(seqNum);
			packet.put(synchronization);
			packet.put(finishConnection);
			packet.put(ack);
			packet.put(mode);
			packet.putInt(window);
			packet.putShort(length);
			byte[] data = packet.array();
			short checkSum = checkSum(data);
			packet = ByteBuffer.allocate(20);
			packet.putShort(checkSum);
			packet.put(data);
		}
	}
	public byte[] getPacket(){
		return packet.array();
	}
	public short checkSum(byte[] data) {
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
	public boolean checkData(byte[] data, short sum, int end){
		short checkSum = 0;
		byte A = 0;
		byte B = 0;
		for(int i = 2; i<end; i++){
			A+= data[i];
			B+= A;
		}
		checkSum = A;
		checkSum = (short) (checkSum<<8);
		checkSum+= B;
		return (checkSum == sum);
	}
}
