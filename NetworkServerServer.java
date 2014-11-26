import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;


public class NetworkServerServer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		RecieverFileHandler rfh = null;
		DatagramSocket serverSocket = new DatagramSocket(4343);
		byte[] receiveData = new byte[1024];
		do{
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			//System.out.println(receiveData);

			ByteBuffer gotten = ByteBuffer.wrap(receiveData);
			if(!checkData(receiveData, gotten.getShort(0), gotten.getShort(14)+16)){
				continue;
			}
			System.out.println("checkSum:" + gotten.getShort(0));
			System.out.println("source:" + gotten.getShort(2));
			System.out.println("dest:" + gotten.getShort(4));
			System.out.println("seqNum:" + gotten.getInt(6));
			System.out.println("synch:" + gotten.get(10));
			System.out.println("finConn:" + gotten.get(11));
			System.out.println("ack:" + gotten.get(12));
			System.out.println("mode:" + gotten.get(13));
			short dataLength = gotten.getShort(14);
			System.out.println("len:" + dataLength);
			if(dataLength>0){
				byte[] data = new byte[dataLength];
				for(int i = 16; i<(16+dataLength); i++){
					data[i-16] = receiveData[i];
				}
				if(rfh==null){
					String reception = new String(data);
					System.out.println(reception);
					String[] fileData = reception.split("!");
					rfh = new RecieverFileHandler(fileData[1], Integer.parseInt(fileData[0]));
				}else{
					rfh.addData(data, gotten.getInt(6));
				}
				//System.out.println("name:" + new String(name));
			}
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			receiveData[12] = 1;
			short checkSum = checkSum(receiveData);
			ByteBuffer cksm = ByteBuffer.allocate(2);
			cksm.putShort(checkSum);
			byte[] checkSumbytes = cksm.array();
			receiveData[0] =checkSumbytes[0];
			receiveData[1] =checkSumbytes[1];
			DatagramPacket sendPacket =
					new DatagramPacket(receiveData,receiveData.length, IPAddress, port);
			serverSocket.send(sendPacket);

		} while(true);
	}
	public static boolean checkData(byte[] data, short sum, int end){
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
	public static short checkSum(byte[] data){
		short checkSum = 0;
		byte A = 0;
		byte B = 0;
		for(int i = 2; i<data.length; i++){
			A+= data[i];
			B+= A;
		}
		checkSum = A;
		checkSum = (short) (checkSum<<8);
		checkSum+= B;
		return (checkSum);
	}
}
