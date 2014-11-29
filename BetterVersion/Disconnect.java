import java.util.Scanner;


public class Disconnect implements Runnable{
	Thread t;
	Scanner sc;
	public Disconnect(Thread thread, Scanner scan){
		sc = scan;
		thread = t;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(sc.hasNextLine()){
				if(sc.nextLine().equals("disconnect")){
					t.interrupt();
					return;
				}
			}
		}
	}

}
