/*
 * Autor:       Daniel de Souza Baulé
 * Projeto:     Trabalho 2 - INE5410 - Programação Concorrente
 * Arquivo:     Classe para implementação do cliente
 */
// Package
package HashClient;

// Class Declaration
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashClient {

    // Atributes
    private boolean done;
    private Socket socket;

    // Constructor
    public HashClient() {
        done = true;
    }

    // Methods
    public void execute() {
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 5000);
            ObjectInputStream input;
            input = new ObjectInputStream(socket.getInputStream());
            
            Thread hashTesterThread = startHashTesterThread();

            String mensagem;
            int initialValue, finalValue;

            while (true) {
                mensagem = (String) input.readObject();

                if (mensagem.equals("END!"))
                    break;
                
                initialValue = input.readInt();
                finalValue = input.readInt();
                
                if (mensagem.equals("DONE!"))
                    done = true;
                
                done = false;
            }
            
            
        try { input.close(); } catch (IOException e) { e.printStackTrace(); }
        try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        try { hashT.join(); } catch (InterruptedException e) { e.printStackTrace(); }

        } catch (IOException ex) {
            Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Thread startHashTesterThread() throws IOException {
        ObjectOutputStream output;
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        
        
        Thread hashTesterThread = new Thread() {
            public void run() {
                output.writeObject("GO!");
                output.flush();
                
                try { output.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        };
        
        hashTesterThread.start();
        return hashTesterThread;
    }
}
