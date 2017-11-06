/*
 * Autor:       Daniel de Souza Baulé
 * Projeto:     Trabalho 2 - INE5410 - Programação Concorrente
 * Arquivo:     Classe principal do servidor
 */
package HashServer;

import static Server.Server.MAX_CLIENTES;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashServerMain {

    //! Numero máximo de clientes conectados
    public static final int MAX_CLIENTES = 100;
    
    //! Lista de códigos a encontrar
    protected List<String> codigos;
    
    //! Construtor
    public HashServerMain() {
    }

    //! Executavel (Servidor)
    public void execute() {
        System.out.println("Executando servervidor!");

        try {
            //! Carrega a lista de códigos a encontrar
            codigos = Files.readAllLines(Paths.get("hashes.txt"));

            ServerSocket serverSocket = new ServerSocket(5000, MAX_CLIENTES);
            ExecutorService exec = Executors.newCachedThreadPool();

            while (true) {
                //Recebe uma nova conexão
                Socket socket = serverSocket.accept();
                System.out.println("Nova conexão realizada - Cliente: " + socket.getInetAddress());

                //Inicia uma thread pra ficar ouvindo as mensagens do cliente
                exec.execute(new HashServerClientThread(socket, codigos));
            }
        } catch (IOException e) {

        }
    }
}
