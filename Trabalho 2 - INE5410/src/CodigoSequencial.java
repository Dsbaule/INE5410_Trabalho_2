import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CodigoSequencial
{
    public static final int MAX_NUMERO = 9999999;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException
    {
        //Carrega a lista de códigos a encontrar
        List<String> codigos = Files.readAllLines(Paths.get("hashes.txt"));

        //Para cada código da lista...
        for (String codigo : codigos)
        {
            long tempo = System.currentTimeMillis();
            for (int i = 0; i <= MAX_NUMERO; i++)
            {
                //Formata i com 7 casas (ex.: 0000000)
                String numero = String.format("%07d", i);
                //Calcula o MD5 desse número
                String md5 = md5(numero);

                //Verifica se o código produzido é igual ao do arquivo
                if (md5.equals(codigo))
                {
                    System.out.println("O código " + codigo + " é produzido pelo número " + numero);
                    tempo = System.currentTimeMillis() - tempo;
                    System.out.println("O programa levou " + tempo + "ms para encontrar esse número.");
                }
            }

        }
    }

    //Esta função produz um hash MD5 para a string de entrada
    public static String md5(String entrada) throws NoSuchAlgorithmException
    {
        MessageDigest sha1 = MessageDigest.getInstance("MD5");
        byte[] saida = sha1.digest(entrada.getBytes());
        StringBuilder saidaStr = new StringBuilder();
        for (byte b : saida)
            saidaStr.append(String.format("%02x", b));
        return saidaStr.toString();
    }
}