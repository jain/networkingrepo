import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.Timer;


public class CheckClientReception implements Runnable{
	private NetworkClientSetup ncs;
	private DatagramSocket clientSocket;
	//private NetworkServerSetup nss;
	public CheckClientReception(NetworkClientSetup ncs, DatagramSocket clientSocket){
		super();
		this.ncs = ncs;
		this.clientSocket = clientSocket;
		//this.nss = nss;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!ncs.ftpComplete()){
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(receivePacket);
				NetworkServerSetup.handle(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		clientSocket.close();
	}
}