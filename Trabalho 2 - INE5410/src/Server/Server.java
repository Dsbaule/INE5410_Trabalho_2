package Server;

import Data.ClientParameters;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static final int FAIXA = 200000;
    public static final int MAX_NUMERO = 9999999;

    public static final int MAX_CLIENTES = 100;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000, MAX_CLIENTES);

            Socket socket = serverSocket.accept();
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            //Carrega a lista de códigos a encontrar
            List<String> codigos = Files.readAllLines(Paths.get("hashes.txt"));

            //Para cada código da lista...
            for (String codigo : codigos) {
                for (int i = 0; i <= MAX_NUMERO; i += FAIXA) {
                    if(((String)input.readObject()).equals("DONE!"))
                        break;
                    ClientParameters parametros = new ClientParameters(codigo, i, i + FAIXA - 1);
                    output.writeObject(parametros);
                    output.flush();
                    System.out.println("Enviado: " + parametros.getCodigo() + " - " + parametros.getInitialValue() + " - " + parametros.getFinalValue());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
