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
		RecieverFileHandler rfh = new RecieverFileHandler();
		DatagramSocket serverSocket = new DatagramSocket(4343);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while(true)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			//System.out.println(receiveData);
			ByteBuffer gotten = ByteBuffer.wrap(receiveData);
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
				byte[] name = new byte[dataLength];
				for(int i = 16; i<(16+dataLength); i++){
					name[i-16] = receiveData[i];
				}
				System.out.println("name:" + new String(name));
			}
			serverSocket.receive(receivePacket);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			DatagramPacket sendPacket =
					new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

		}
	}

}
