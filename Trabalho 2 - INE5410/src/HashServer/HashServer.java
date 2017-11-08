/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// Package
package HashServer;

// Imports
import Data.ClientParameters;
import Data.HashCode;
import Data.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Class Declaration
public class HashServer {

    // Atributes
    private List<String> hashCodes;
    private List<ObjectOutputStream> clients;

    private int port;

    private static final int FAIXA = 200000;
    private static final int MAX_NUMERO = 9999999;

    private int initial_value = 0;
    private int index;
    private String currentHash;

    // Constructor
    public HashServer(int port) {
        this.index = 0;
        this.port = port;
        this.clients = new ArrayList<>();
    }

    // Methods
    public void execute() {
        try {
            //Carrega a lista de códigos a encontrar
            hashCodes = Files.readAllLines(Paths.get("hashes.txt"));

            System.out.println("Aguardando conexões...");
            //Cria um socket para receber conexões na porta 5000
            ServerSocket server = new ServerSocket(5000, 10);

            while (true) {
                //Recebe uma nova conexão
                Socket socket = server.accept();
                System.out.println("Nova conexão realizada");

                //Inicia uma thread pra ficar ouvindo as mensagens do cliente
                startServerClientThread(socket);
            }

        } catch (IOException ex) {
            Logger.getLogger(HashServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Thread startServerClientThread(Socket socket) throws IOException {
        ObjectInputStream input;
        input = new ObjectInputStream(socket.getInputStream());

        ObjectOutputStream output;
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();

        Thread serverClientThread = new Thread() {
            public void run() {
                try {

                    synchronized (clients) {
                        clients.add(output);
                    }

                    int clientMessage;
                    ClientParameters parameters = null;

                    while (true) {
                        clientMessage = input.readInt();

                        switch (clientMessage) {
                            case Protocol.HASHCODE_FOUND:
                                String numero = (String) input.readObject();
                                System.out.println("O código " + parameters.getCodigo() + " é produzido pelo número " + numero);

                                for (ObjectOutputStream out : clients) {
                                    if (!out.equals(output)) {
                                        out.writeInt(Protocol.HASHCODE_FOUND);
                                        out.writeObject(parameters.getCodigo());
                                        out.flush();
                                    }
                                }
                                break;
                            case Protocol.NEXT_HASHCODE:
                                parameters = getNextParameters();
                                System.out.println("Enviando o código " + parameters.getCodigo() + " no intervalo " + parameters.getInitialValue() + " - " + parameters.getFinalValue());
                                output.writeInt(Protocol.NEXT_HASHCODE);
                                output.writeObject(parameters);
                                output.flush();
                                break;
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(HashServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(HashServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        serverClientThread.start();

        return serverClientThread;
    }

    private synchronized ClientParameters getNextParameters() {
        ClientParameters clientParameters;

        clientParameters = new ClientParameters(hashCodes.get(index), initial_value, (initial_value + FAIXA - 1));

        initial_value += FAIXA;

        if (initial_value > MAX_NUMERO) {
            index++;
            initial_value = 0;
        }

        return clientParameters;
    }

    private boolean done(int index) {
        return index > hashCodes.size();
    }
}
