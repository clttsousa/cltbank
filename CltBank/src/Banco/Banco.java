package Banco;

import Clientes.Cliente;
import Menus.MenuConta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Banco {
    private List<Conta> contas;
    private Scanner scanner;

    public Banco() {
        contas = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void executar() {
        boolean sair = false;

        while (!sair) {
            int opcao = exibirMenuInicial();

            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    criarConta();
                    break;
                case 3:
                    exibirContasExistentes();
                    break;
                case 4:
                    sair = true;
                    System.out.println("Obrigado por utilizar nosso banco. Até a proxima!");
                    break;

            }
        }
    }


    private int exibirMenuInicial() {
        System.out.println("*******************CLTBANK*******************");
        System.out.println("1- Login na conta");
        System.out.println("2- Criar conta");
        System.out.println("3- Consultas contas existentes");
        System.out.println("4- Sair");
        System.out.println("*********************************************");
        System.out.println("Digite uma opção: ");
        return scanner.nextInt();
    }

    private void fazerLogin() {
        System.out.print("Digite o número da conta: ");
        int numeroConta = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        Conta conta = buscarConta(numeroConta);
        if (conta != null && conta.verificarSenha(senha)) {
            System.out.println("Login realizado com sucesso!");
            MenuConta menuConta = new MenuConta();
            menuConta.exibirMenu(conta, contas);
        } else {
            System.out.println("Número da conta ou senha inválidos. Login não realizado.");
        }
    }

    private Conta buscarConta(int numeroConta) {
        for (Conta conta : contas) {
            if (conta.getNumeroConta() == numeroConta) {
                return conta;
            }
        }
        return null;
    }

    private void criarConta() {
        //Solicitar as informacoes
        System.out.println("******************************************");
        System.out.println("Digite o primeiro nome do cliente: ");
        String nome = scanner.next();
        scanner.nextLine();
        System.out.println("Digite o sobrenome do cliente");
        String sobrenome = scanner.nextLine();
        System.out.println("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.println("Digite o CEP do cliente: ");
        String cep = scanner.nextLine();
        String endereco = consultarCEP(cep);
        if (endereco != null) {
            System.out.println("Endereço encontrado: " + endereco);
            System.out.println("Digite o numero da casa: ");
            String numeroCasa = scanner.nextLine();
            System.out.println("Digite o telefone do cliente: ");
            String telefone = scanner.nextLine();
            System.out.println("Digite o email do cliente: ");
            String email = scanner.nextLine();

            //passo isso ele ira criar o cliente
            Cliente cliente = new Cliente(nome, sobrenome, cpf, endereco, numeroCasa, telefone, email);

            //aqui ele solicita a senha e o saldo inicial da conta
            System.out.println("Digite uma senha para a conta: ");
            String senha = scanner.nextLine();
            System.out.println("Digite o saldo inicial da conta: ");
            double saldoInicical = scanner.nextDouble();

            //gerar numero da conta
            int numeroConta = gerarNumeroConta();

            //Criar conta associado a um cliente
            Conta conta = new Conta(numeroConta, cliente, senha, saldoInicical);
            contas.add(conta);
            System.out.println("Conta criada com sucesso, " +
                    "numero gerado para a conta é: " + numeroConta);
        } else {
            System.out.println("CEP invalido. Não foi possivel criar a conta.");
        }
    }

    private String consultarCEP(String cep) {
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response to extract the relevant information
                // In this example, we assume the response has the following format: {"cep": "01001000", "logradouro": "Avenida Paulista", ...}
                String logradouro = extractValueFromJSON(response.toString(), "logradouro");
                String cidade = extractValueFromJSON(response.toString(), "localidade");
                String estado = extractValueFromJSON(response.toString(), "uf");

                return logradouro + " - " + cidade + "/" + estado;
            } else {
                System.out.println("Erro ao consultar o CEP. Código de resposta: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String extractValueFromJSON(String json, String key) {
        int startIndex = json.indexOf("\"" + key + "\":");
        if (startIndex != -1) {
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }

            if (endIndex != -1) {
                int valueStartIndex = startIndex + key.length() + 3;
                String value = json.substring(valueStartIndex, endIndex);
                return value.replaceAll("\"", "");
            }
        }
        return null;
    }

    private int gerarNumeroConta(){
        Random random = new Random();
        int numero = random.nextInt(9000) + 1000;
        while (existeNumeroConta(numero)){
            numero = random.nextInt(9000) + 1000;
        }
        return numero;
    }


    private boolean existeNumeroConta (int numeroConta){
        for (Conta conta : contas) {
            if (conta.getNumeroConta() == numeroConta){
                return true;
            }
        }
        return false;
    }

    private void exibirContasExistentes(){
        if(contas != null) {
            System.out.println("***************** CONTAS EXISTENTES NO BANCO *****************");
            for (Conta conta : contas) {
                System.out.println(conta.getNumeroConta() + " - " + conta.getCliente().getNome() + " " +conta.getCliente().getSobrenome());
                System.out.println("Endereço do cliente: " + conta.getCliente().getEndereco());
                System.out.println("Telefone do cliente: " + conta.getCliente().getTelefone());
                System.out.println("\n*************************************************************");
            }
        } else {
            System.out.println("Ainda não a contas criadas no banco.");
        }
    }
    public List<Conta> getContas() {
        return contas;
    }


}
