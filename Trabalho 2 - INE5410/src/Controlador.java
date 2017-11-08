/*
 * Autor:       Daniel de Souza Baulé
 * Projeto:     Trabalho 2 - INE5410 - Programação Concorrente
 * Arquivo:     Classe principal para seleção de servidor/cliente
 */

import HashClient.HashClient;
import java.util.Scanner;
import HashServer.HashServer;

public class Controlador {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Deseja iniciar em que modo? [server/cliente]");
            String s = scanner.nextLine();
            if (s.equals("server")) {
                HashServer hashServer = new HashServer(5000);
                hashServer.execute();
                break;
            } else if (s.equals("cliente")) {
                HashClient hashClient = new HashClient("127.0.0.1", 5000);
                hashClient.execute();
                break;
            } else if (s.equals("sair")) {
                break;
            }
        }
    }
}
