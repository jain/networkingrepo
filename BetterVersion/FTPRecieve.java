package Client;

public class FTPRecieve {
	private String fileName;
	public FTPRecieve(String filename){
		fileName = filename;
	}
	public byte[] getFile() {
		// TODO Auto-generated method stub
		return fileName.getBytes();
	}
}
