package Client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientDriver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	/**
	 * 0 = get
	 * 1 = post
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String filename = "";
		for(String s: args){
			System.out.println(s);
		}
		RTP rtp = new RTP(args[0], args[1], args[2]);
		Scanner scan = new Scanner(System.in);
		// ignores if get or post without connect or disconnect
		while(!rtp.isReady()){
			String command = scan.nextLine();
			if(command.equals("connect")){
				rtp.connect();
			}else if(command.equals("disconnect")){
				scan.close();
				return;
			}else if(command.split(" ")[0].equals("window")){
				rtp.setWindow(Integer.parseInt(command.split(" ")[1]));
			} else if(command.split(" ")[0].equals("get")){
				filename = (command.split(" ")[1]);
				FTPRecieve ftp = new FTPRecieve();
				rtp.setMode(0);
			} else if(command.split(" ")[0].equals("post")){
				filename = (command.split(" ")[1]);
				FTPSend ftp = new FTPSend(filename);
				rtp.setMode(1);
			}
		}
		Thread thread = new Thread(rtp);
		thread.start();
		String command = scan.nextLine();
		while(!command.equals("disconnect")&&!rtp.isComplete()){
			command = scan.nextLine();
		}
		if(!rtp.isComplete()){
			thread.interrupt();
		}
	}

}
