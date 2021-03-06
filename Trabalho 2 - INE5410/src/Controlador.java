/*
 * Autor:       Daniel de Souza Baulé
 * Projeto:     Trabalho 2 - INE5410 - Programação Concorrente
 * Arquivo:     Classe principal para seleção de servidor/cliente
 */

import HashClient.HashClient;
import java.util.Scanner;
import HashServer.HashServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controlador {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Deseja iniciar em que modo? [server/cliente]");
            String s = scanner.nextLine();
            if (s.equals("server")) {
                HashServer hashServer;
                try {
                    hashServer = new HashServer(5000);
                    hashServer.execute();
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            } else if (s.equals("cliente")) {
                System.out.println("IP = ");
                String ip = scanner.nextLine();
                if(ip.equals(""))
                    ip = "127.0.0.1";
                HashClient hashClient = new HashClient(ip, 5000);
                hashClient.execute();
                break;
            } else if (s.equals("sair")) {
                break;
            }
        }
    }
}
