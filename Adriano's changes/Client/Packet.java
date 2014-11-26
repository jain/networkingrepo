package Client;

import java.nio.ByteBuffer;


public class Packet {
        private short checksum;
        private short source;
        private short destination;
        private int sequenceNumber;
	private byte synchronization;
        private byte finishConnection;
        private byte acknowledgment;
        private byte mode;
        private short dataLength;
	private byte[] packetData;
        
        public Packet()
        {
            
        }
        
	public Packet(short source, short destination, int sequenceNumber, byte synchronization, 
                byte finishConnection, byte acknowledgment, byte mode, byte[] input)
        {
            setPacket(source, destination, sequenceNumber, synchronization, finishConnection, acknowledgment, mode, input);
	}
        
        // 4001 4000 seqNo syn=1 fin=0 ack=0
	public void setPacket(short source, short destination, int sequenceNumber, byte synchronization, 
                byte finishConnection, byte acknowledgment, byte mode, byte[] input) 
        {	
            this.source = source;
            this.destination = destination;
            this.sequenceNumber = sequenceNumber;
            this.synchronization = synchronization;
            this.finishConnection = finishConnection;
            this.acknowledgment = acknowledgment;
            this.mode = mode;
            this.dataLength = (short) input.length;
            this.packetData = input;
	}
        
	public int getSequenceNumber()
        {
            return sequenceNumber;
	}
        
	private byte[] createPacket() 
        {	
            ByteBuffer packet;
            byte[] packetStream;
            short checkSum;
            
            packet = ByteBuffer.allocate(14 + dataLength);
            packet.putShort(source);
            packet.putShort(destination);
            packet.putInt(sequenceNumber);
            packet.put(synchronization);
            packet.put(finishConnection);
            packet.put(acknowledgment);
            packet.put(mode);
            packet.putShort(dataLength);
            packet.put(packetData);

            packetStream = packet.array();
            
            checkSum = checkSum(packetStream);
            this.checksum = checkSum;
            
            packet = ByteBuffer.allocate(16 + dataLength);
            packet.putShort(checkSum);
            packet.put(packetStream);
            
            return packet.array();
	}
        
        void extractPacket(byte[] packetStreamOfBytes)
        { //continue here
            gotten = ByteBuffer.wrap(packetStreamOfBytes);
            if(!checkData(packetStreamOfBytes, gotten.getShort(0), gotten.getShort(14)+16)){
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
                            data[i-16] = packetStreamOfBytes[i];
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
        }

	private static short checkSum(byte[] data) 
        {
            short checkSum = 0;
            byte A = 0;
            byte B = 0;
            for(int i = 0; i < data.length; i++){
                    A+= data[i];
                    B+= A;
            }
            checkSum = A;
            checkSum = (short) (checkSum << 8);
            checkSum+= B;
            return checkSum;
        }
        
	public byte[] getPacketData() 
        {
            return packetData;
	}
	public void setData(byte[] packet) 
        {
            this.packetData = packet;
	}
}