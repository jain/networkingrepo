package Server;

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
		ReceiverFileHandler rfh = null;
		DatagramSocket serverSocket = new DatagramSocket(4001);
		byte[] receiveData = new byte[1024];
                
                DatagramPacket receivePacket;
                ByteBuffer gotten;
                short dataLength;
                byte[] data;
                InetAddress IPAddress;
                int port;
                short checkSum;
                ByteBuffer cksm;
                byte[] checkSumbytes;
                DatagramPacket sendPacket;
                
		do{
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			//System.out.println(receiveData);

			
                    gotten = ByteBuffer.wrap(receiveData);
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
			
                    dataLength = gotten.getShort(14);
                    System.out.println("len:" + dataLength);
                    if(dataLength>0){

                        data = new byte[dataLength];
                            for(int i = 16; i<(16+dataLength); i++){
                                    data[i-16] = receiveData[i];
                            }
                            if(rfh==null&&gotten.getInt(6)==0){
                                    String reception = new String(data);
                                    System.out.println(reception);
                                    String[] fileData = reception.split("!");
                                    rfh = new ReceiverFileHandler(fileData[1], Integer.parseInt(fileData[0]));
                            }else{
                                    rfh.addData(data, gotten.getInt(6));
                            }
                            //System.out.println("name:" + new String(name));
                    }
			
                    IPAddress = receivePacket.getAddress();
			
                    port = receivePacket.getPort();
			receiveData[12] = 1;
			
                    checkSum = checkSum(receiveData);
			
                    cksm = ByteBuffer.allocate(2);
			cksm.putShort(checkSum);
			
                    checkSumbytes = cksm.array();
			receiveData[0] =checkSumbytes[0];
			receiveData[1] =checkSumbytes[1];
			
                    sendPacket = new DatagramPacket(receiveData,receiveData.length, IPAddress, 8000);
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
