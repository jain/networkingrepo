package Client;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerDriver {

	/**
	 * @param args
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws SocketException, UnknownHostException {
		// TODO Auto-generated method stub
		for(String s: args){
			System.out.println(s);
		}
		Server server = new Server(args[0], args[1], args[2]);
		Scanner scan = new Scanner(System.in);
		if(!server.isPossible()) return;
		// ignores if get or post without connect or disconnect
		Thread thread = new Thread(server);
		thread.start();
		String command = scan.nextLine();
		while(!command.equals("terminate")){
			if(command.split(" ")[0].equals("window")){
				server.setWindow(Integer.parseInt(command.split(" ")[1]));
			}
			command = scan.nextLine();
		}	
		thread.interrupt();
	}
}