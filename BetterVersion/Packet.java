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
		packet = ByteBuffer.wrap(receiveData);
		checkSum = packet.getShort(0);
		source = packet.getShort(2);
		dest = packet.getShort(4);
		seqNum = packet.getInt(6);
		synchronization = packet.get(10);
		finishConnection = packet.get(11);
		ack = packet.get(12);
		mode = packet.get(13);
		window = packet.getInt(14);
		length = packet.getShort(18);
		if(length>0){
			data = new byte[length];
			for(int i = 20; i<(20+length); i++){
				data[i-20] = receiveData[i];
			}
			/*if(rfh==null&&packet.getInt(6)==0){
				String reception = new String(data);
				System.out.println(reception);
				String[] fileData = reception.split("!");
				rfh = new RecieverFileHandler(fileData[1], Integer.parseInt(fileData[0]));
			}else{
				rfh.addData(data, packet.getInt(6));
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
			this.data = input;
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
			byte[] tmp = packet.array();
			checkSum = checkSum(tmp);
			packet = ByteBuffer.allocate(20+length);
			packet.putShort(checkSum);
			packet.put(tmp);
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
			byte[] tmp = packet.array();
			short checkSum = checkSum(tmp);
			packet = ByteBuffer.allocate(20);
			packet.putShort(checkSum);
			packet.put(tmp);
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
