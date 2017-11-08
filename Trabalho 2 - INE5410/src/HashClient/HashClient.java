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
    private boolean testHashCode;
    private Socket socket;

    private final String ip;
    private final int port;

    private ClientParameters parameters;

    // Constructor
    public HashClient(String ip, int port) {
        testHashCode = false;
        this.ip = ip;
        this.port = port;
    }

    // Methods
    public void execute() {
        try {
            System.out.println("Conectando ao servidor: " + ip + ":" + port);
            socket = new Socket(InetAddress.getByName(ip), port);

            ObjectOutputStream output;
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            ObjectInputStream input;
            input = new ObjectInputStream(socket.getInputStream());

            Thread hashTesterThread = startHashTesterThread(output);

            int serverResponse;
            String hashCode;

            WHILE:
            while (true) {

                System.out.println("Aguardando resposta do servidor!");
                serverResponse = input.readInt();

                switch (serverResponse) {
                    case Protocol.DONE:
                        //System.out.println("DONE!");
                        testHashCode = false;
                        break WHILE;
                    case Protocol.HASHCODE_FOUND:
                        //System.out.println("HASHCODE_FOUND!");
                        hashCode = (String) input.readObject();
                        if (!parameters.getCodigo().equals(hashCode)) {
                            break;
                        }
                        testHashCode = false;
                        break;
                    case Protocol.NEXT_HASHCODE:
                        //System.out.println("NEXT_HASHCODE!");
                        parameters = (ClientParameters) input.readObject();
                        testHashCode = true;
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
                testHashCode = true;
                hashTesterThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Conexão Encerrada.");
    }

    private Thread startHashTesterThread(ObjectOutputStream output) throws IOException {

        Thread hashTesterThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        output.writeInt(Protocol.NEXT_HASHCODE);
                        output.flush();

                        while (!testHashCode) {
                            Thread.sleep(0, 1);
                        }

                        System.out.println("Testando o código " + parameters.getCodigo() + " no intervalo " + parameters.getInitialValue() + " - " + parameters.getFinalValue());

                        for (int i = parameters.getInitialValue(); (i <= parameters.getFinalValue()) && testHashCode; i++) {
                            //Formata i com 7 casas (ex.: 0000000)
                            String numero = String.format("%07d", i);
                            //Calcula o MD5 desse número
                            String md5 = md5(numero);

                            //Verifica se o código produzido é igual ao do arquivo
                            if (parameters.matchesCodigo(md5)) {
                                output.writeInt(Protocol.HASHCODE_FOUND);
                                output.writeInt(i);
                                output.flush();
                                System.out.println("O código " + parameters.getCodigo() + " é produzido pelo número " + numero);
                            }
                        }

                        testHashCode = false;
                    }
                } catch (IOException | NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println("Encerrando!");
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
