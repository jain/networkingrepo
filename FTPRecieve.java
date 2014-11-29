public class FTPRecieve {
	private String fileName;
	private int numOfPackets;
	public FTPRecieve(String filename){
		fileName = filename;
	}
	public byte[] getFile() {
		// TODO Auto-generated method stub
		return fileName.getBytes();
	}
	public int getNumOfPackets() {
		return numOfPackets;
	}
	public void setNumOfPackets(int numOfPackets) {
		this.numOfPackets = numOfPackets;
	}
}
