/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliabletransportprotocol;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author adriano
 */
public interface ReliableTransportProtocolFunctions 
{   
    DatagramSocket prepareConnection(InetAddress IPAddress, short port);
    boolean closeConnection(DatagramSocket socket);
    
    // Server-side method
    // Prepares and handles the three-way handshake, receiving the first request and sending ack back
    boolean acceptConnection(DatagramSocket serverSocket, InetAddress clientIPAddress, short clientPort);
    
    // Client-side method
    // Prepares and handles the three way handshake in the client, sending the first request and waiting for ack
    boolean createConnection(DatagramSocket clientSocket, InetAddress serverIPAddress, short serverPort);
    
    // Sends data to the other peer that is already connected
    int sendData(byte[] outgoingData, int sizeOfBuffer);
    // Receives data from peer that is connected 
    int receiveData(byte[] incomingData, int maximumSizeOfBuffer);
    
    boolean isClient();
    boolean isServer();
}
