import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class NetworkServerSetup {
	public static void main(String[] args) throws IOException {
		FileInputStream fs = new FileInputStream("cracking_coding.pdf");
		FileOutputStream os = new FileOutputStream("copy.pdf");
		int b;
		while ((b = fs.read()) != -1) {
		    os.write(b);
		}
		os.close();
		fs.close();
		/*File file = new File("cracking_coding.pdf");
		FileReader reader = new FileReader(file);
		char[] chars = new char[(int) file.length()];
		reader.read(chars);
		String content = new String(chars);
		reader.close();
		NetworkClientSetup ncs = new NetworkClientSetup(null);
		ncs.breakFile(content, 1000);
		PrintWriter writer = new PrintWriter("copy.pdf");
		writer.println(content);
		writer.close();*/
		/*try {
			int port = Integer.parseInt(args[0]);
			if (!(port >= 0 && port <= 65535)) {
				System.out.println("Invalid command");
				return;
			}
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());
				int command = Integer.parseInt(sentence.split(" ")[0]);
				int input1 = Integer.parseInt(sentence.split(" ")[1]);
				int input2 = Integer.parseInt(sentence.split(" ")[2]);
				String output = "Cannot compute";
				if (command == 0) {
					if (input1 + input2 < 65536) {
						output = (input1 + input2) + "";
					}
				} else {
					if (input1 * input2 < 65536) {
						output = (input1 * input2) + "";
					}
				}
				InetAddress IPAddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				sendData = output.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
		} catch (SocketException e) {
			System.out.println("Invalid command");
		} catch (IOException e) {
			System.out.println("Invalid command");
		} catch (Exception e){
			System.out.println("Invalid command");
		}*/

	}
}