import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class CheckClientReception implements Runnable{
	private NetworkClientSetup ncs;
	private DatagramSocket clientSocket;
	public CheckClientReception(NetworkClientSetup ncs, DatagramSocket clientSocket){
		super();
		this.ncs = ncs;
		this.clientSocket = clientSocket;
	}
	@Override
	public void run() {
		while(!ncs.ftpComplete()){
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				clientSocket.receive(receivePacket);
				NetworkServerSetup.handle(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		clientSocket.close();
	}
}