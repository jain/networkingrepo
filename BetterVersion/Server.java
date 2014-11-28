package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Client.Packet;

public class Server implements Runnable{
	private short myPort;
	private String ip;
	private short port;
	private int window;
	private boolean possible;
	private DatagramSocket serverSocket;
	private InetAddress IPAddress;
	private byte mode;
	public Server(String myPort, String ip, String port) throws SocketException, UnknownHostException{
		// TODO Auto-generated constructor stub
		mode = -1;
		window = Integer.MAX_VALUE;
		this.myPort = Short.parseShort(myPort);
		if (!(this.myPort >= 0 && this.myPort <= 65535&&(this.myPort & 1) == 1)) {
			System.out.println("Invalid command");
			possible = false;
			return;
		}
		this.port = Short.parseShort(port);
		if (!(this.port >= 0 && this.port <= 65535)) {
			System.out.println("Invalid command");
			possible = false;
			return;
		}
		this.ip = ip;
		if (!check(this.ip)) {
			System.out.println("Invalid command");
			possible = false;
			return;
		}
		possible = true;
		serverSocket = new DatagramSocket(this.myPort);
		IPAddress = InetAddress.getByName("localhost");
	}
	private boolean check(String ip) {
		// TODO Auto-generated method stub
		try {
			String p1 = ip.split("\\.")[0];
			String p2 = ip.split("\\.")[1];
			String p3 = ip.split("\\.")[2];
			String p4 = ip.split("\\.")[3];
			if (!(p1 + "." + p2 + "." + p3 + "." + p4).equals(ip)) {
				return false;
			}
			int ip1 = Integer.parseInt(p1);
			int ip2 = Integer.parseInt(p2);
			int ip3 = Integer.parseInt(p3);
			int ip4 = Integer.parseInt(p4);
			if (!(ip1 >= 0 && ip1 <= 255)) {
				return false;
			}
			if (!(ip2 >= 0 && ip2 <= 255)) {
				return false;
			}
			if (!(ip3 >= 0 && ip3 <= 255)) {
				return false;
			}
			if (!(ip4 >= 0 && ip4 <= 255)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public void setWindow(int win){
		window = win;
		System.out.println(win);
	}
	@Override
	public void run(){
		// TODO Auto-generated method stub
		while(!Thread.interrupted()){
			try{
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				Packet packet = new Packet(receiveData);
				if(!packet.checkData(receiveData, packet.checkSum)) continue;
				if(packet.synchronization==1&&packet.mode==-1) acceptConnection(packet);
				if(packet.synchronization==1) changeMode(packet);
			} catch (IOException e){

			}
		}
	}
	private void changeMode(Packet packet) throws IOException {
		// TODO Auto-generated method stub
		mode = packet.mode;
		Packet send = new Packet(null,myPort, port,(byte)-1,(byte)1,(byte)0,(byte)1, mode, window);
		byte[] toSend = send.getPacket();
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
		serverSocket.send(sendPacket);
	}
	private void acceptConnection(Packet packet) throws IOException {
		Packet send = new Packet(null,myPort, port,(byte)-1,(byte)1,(byte)0,(byte)1,packet.mode, window);
		byte[] toSend = send.getPacket();
		DatagramPacket sendPacket = new DatagramPacket(toSend, toSend.length, IPAddress, port);
		serverSocket.send(sendPacket);
	}
	public boolean isPossible() {
		return possible;
	}
}
