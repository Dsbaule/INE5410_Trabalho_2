
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Data.ClientParameters;

public class Client {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        try {
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 5000);
            ObjectOutputStream output;
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            ObjectInputStream input;
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject("GO!");
            output.flush();

            while (true) {
                ClientParameters parametros = (ClientParameters) input.readObject();

                System.out.println("Recebido: " + parametros.getCodigo() + " - " + parametros.getInitialValue() + " - " + parametros.getFinalValue());

                for (int i = parametros.getInitialValue(); i <= parametros.getFinalValue(); i++) {
                    //Formata i com 7 casas (ex.: 0000000)
                    String numero = String.format("%07d", i);
                    //Calcula o MD5 desse número
                    String md5 = md5(numero);

                    //Verifica se o código produzido é igual ao do arquivo
                    if (parametros.matchesCodigo(md5)) {
                        System.out.println("MARCA! 1");
                        output.writeObject((String) "DONE!");
                        output.flush();
                        System.out.println("O código " + parametros.getCodigo() + " é produzido pelo número " + numero);
                        break;
                    }
                }
                
                System.out.println("MARCA! 2");
                output.writeObject((String) "GO!");
                output.flush();

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Esta função produz um hash MD5 para a string de entrada
    public static String md5(String entrada) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("MD5");
        byte[] saida = sha1.digest(entrada.getBytes());
        StringBuilder saidaStr = new StringBuilder();
        for (byte b : saida) {
            saidaStr.append(String.format("%02x", b));
        }
        return saidaStr.toString();
    }
}
