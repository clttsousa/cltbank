package Menus;

import Banco.Conta;
import java.util.Scanner;
import java.util.List;

public class MenuConta {
    private Scanner scanner;
    public MenuConta(){
        scanner = new Scanner(System.in);
    }

    public void exibirMenu (Conta conta, List<Conta> contas){
        boolean sair = false;
        while(!sair){
            System.out.println("************************** " + conta.getNumeroConta() + " - " +
                    conta.getCliente().getNome() + " " +conta.getCliente().getSobrenome() + " **************************");
            System.out.println("1- DEPOSITAR");
            System.out.println("2- SACAR");
            System.out.println("3- TRANSFERIR");
            System.out.println("4- MOSTRAR SALDO");
            System.out.println("5- VOLTAR");
            System.out.println("*****************************************************");
            System.out.println("DIGITE UMA OPÇÃO: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao){
                case 1:
                    System.out.println("Digite o valor a ser depositado: ");
                    double valorDeposito = scanner.nextDouble();
                    conta.depositar(valorDeposito);
                    System.out.println("Valor depositado com sucesso");
                    break;
                case 2:
                    System.out.println("Digite o valor a ser sacado: ");
                    double valorSacado = scanner.nextDouble();
                    if(conta.sacar(valorSacado)){
                        System.out.println("Saldo sacado com sucesso!");
                    }else{
                        System.out.println("Saldo insuficiente para saque!");
                    }
                    break;
                case 3:
                    System.out.println("Digite o numero da conta para que deseja fazer a transferencia: ");
                    int numeroContaDestino = scanner.nextInt();
                    Conta contaDestino = buscarConta(numeroContaDestino, contas);
                    System.out.println("Digite  o valor a ser transferido: ");
                    double valorTransferido = scanner.nextDouble();
                    boolean transferenciaRealizada = realizarTransferencia(conta, numeroContaDestino, valorTransferido, contas);
                    if (transferenciaRealizada){
                        System.out.println("Transferencia realizada com sucesso.");
                    } else {
                        System.out.println("Não foi possivel realizar a transferencia");
                    }
                    break;
                case 4:
                    System.out.println("Seu saldo é de: " + conta.getSaldo());
                    break;
                case 5:
                    sair = true;
                    System.out.println("Voltando ao menu inicial");
                    break;
                default:
                    System.out.println("Opção invalida, por favor escolha o opção valida.");
                    break;
            }
        }
    }
    private boolean realizarTransferencia(Conta origem, int numeroContaDestino, double valor, List<Conta> contas) {
        Conta destino = buscarConta (numeroContaDestino, contas);
        if (destino != null) {
            return origem.transferir(destino, valor);
        }
        return false;
    }
    private Conta buscarConta(int numeroConta, List<Conta> contas) {
        for (Conta conta : contas) {
            if (conta.getNumeroConta() == numeroConta) {
                return conta;
            }
        }
        return null;
    }
}
