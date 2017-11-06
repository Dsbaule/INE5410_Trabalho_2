/*
 * Autor:       Daniel de Souza Baulé
 * Projeto:     Trabalho 2 - INE5410 - Programação Concorrente
 * Arquivo:     Classe do servidor para comunicação com Cliente
 */
package HashServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashServerClientThread implements Runnable {

    public static final int FAIXA = 200000;
    public static final int MAX_NUMERO = 9999999;

    private Socket serverSocket;
    private List<String> codigos;

    public HashServerClientThread(Socket serverSocket, List<String> codigos) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            //! Criação dos Streams para o cliente
            ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());
            out.flush();
            ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());
            
            //! String para recebimento de mensagens do cliente
            String message = null;
                
            //! Loop para comunicação com o cliente
            while(true) {
                message = (String) input.readObject();
                
                
            }

        } catch (IOException ex) {
            Logger.getLogger(HashServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HashServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
