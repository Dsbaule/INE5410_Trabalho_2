/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Package
package HashServer;

// Imports
import Data.HashCode;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Class Declaration
public class HashServer {
    // Atributes
    private List<HashCode> hashCodes;
    private List<ObjectOutputStream> clients;
    
    private int port;
    
    // Constructor
    public HashServer(int port) {
        this.port = port;
    }   
    
    // Methods
    public void execute() {
        clients = new ArrayList<>();
        hashCodes = new ArrayList<>();
    }

    public Thread startServerClientThread(Socket socket) throws IOException {
        ObjectInputStream input;
        input = new ObjectInputStream(socket.getInputStream());

        ObjectOutputStream output;
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        
        Thread serverClientThread = new Thread() {
            public void run() {
                
            }
        };
        
        serverClientThread.start();
                
        return serverClientThread;
    }
}
