/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliabletransportprotocol;

/**
 *
 * @author adriano
 */
public interface FIleTransferApplicationFunctions {
    boolean connect();
    boolean getFile(String filename);
    boolean postFile(String filename);
    void setWindowSize(int windowSize);
    boolean disconnect();
}
