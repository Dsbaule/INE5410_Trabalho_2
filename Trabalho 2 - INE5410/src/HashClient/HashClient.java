/*
 * Autor:       Daniel de Souza Baulé
 * Projeto:     Trabalho 2 - INE5410 - Programação Concorrente
 * Arquivo:     Classe para implementação do cliente
 */
// Package
package HashClient;

// Class Declaration
import Data.ClientParameters;
import Data.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashClient {

    // Atributes
    private boolean testHash;
    private Socket socket;

    private String ip;
    private int port;

    private ClientParameters parameters;

    // Constructor
    public HashClient(String ip, int port) {
        testHash = false;
        this.ip = ip;
        this.port = port;
    }

    // Methods
    public void execute() {
        try {
            socket = new Socket(InetAddress.getByName(ip), port);
            ObjectInputStream input;
            input = new ObjectInputStream(socket.getInputStream());

            ObjectOutputStream output;
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            Thread hashTesterThread = startHashTesterThread(output);

            int serverResponse;

            WHILE:
            while (true) {

                serverResponse = input.readInt();

                switch (serverResponse) {
                    case Protocol.DONE:
                        testHash = false;
                        break WHILE;
                    case Protocol.HASHCODE_FOUND:
                        testHash = false;
                        hashTesterThread.wait();
                    case Protocol.NEXT_HASHCODE:
                        parameters = (ClientParameters) input.readObject();
                        testHash = true;
                        break;

                }

            }

            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hashTesterThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Thread startHashTesterThread(ObjectOutputStream output) throws IOException {

        Thread hashTesterThread = new Thread() {
            public void run() {
                try {
                    while (true) {
                        output.writeInt(Protocol.NEXT_HASHCODE);
                        output.flush();

                        this.notify();
                        while (!testHash);

                        while (testHash) {
                            for (int i = parameters.getInitialValue(); i <= parameters.getFinalValue(); i++) {
                                //Formata i com 7 casas (ex.: 0000000)
                                String numero = String.format("%07d", i);
                                //Calcula o MD5 desse número
                                String md5 = md5(numero);

                                //Verifica se o código produzido é igual ao do arquivo
                                if (parameters.matchesCodigo(md5)) {
                                    output.writeInt(Protocol.HASHCODE_FOUND);
                                    output.writeObject(md5);
                                    output.flush();
                                    System.out.println("O código " + parameters.getCodigo() + " é produzido pelo número " + numero);
                                }
                            }

                        }

                    }

                } catch (IOException ex) {
                    Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        hashTesterThread.start();
        return hashTesterThread;
    }

    //Esta função produz um hash MD5 para a string de entrada
    private static String md5(String entrada) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("MD5");
        byte[] saida = sha1.digest(entrada.getBytes());
        StringBuilder saidaStr = new StringBuilder();
        for (byte b : saida) {
            saidaStr.append(String.format("%02x", b));
        }
        return saidaStr.toString();
    }
}
