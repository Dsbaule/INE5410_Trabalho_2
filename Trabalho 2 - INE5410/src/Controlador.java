/*
 * Autor:       Daniel de Souza Baulé
 * Projeto:     Trabalho 2 - INE5410 - Programação Concorrente
 * Arquivo:     Classe principal para seleção de servidor/cliente
 */

import java.util.Scanner;
import HashServer.HashServerMain;
import HashClient.HashClientMain;

public class Controlador {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Deseja iniciar em que modo? [server/cliente]");
            String s = scanner.nextLine();
            if (s.equals("server")) {
                HashServerMain hashServer = new HashServerMain();
                hashServer.execute();
                break;
            } else if (s.equals("cliente")) {
                HashClientMain hashClient = new HashClientMain();
                hashClient.execute();
                break;
            } else if (s.equals("sair")) {
                break;
            }
        }
    }
}
