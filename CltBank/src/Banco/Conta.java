package Banco;

import Clientes.Cliente;

public class Conta {
    private int numeroConta;
    private Cliente cliente;
    private String senha;
    private double saldo;

    public Conta(int numeroConta, Cliente cliente, String senha, double saldo) {
        this.numeroConta = numeroConta;
        this.cliente = cliente;
        this.senha = senha;
        this.saldo = saldo;
    }

    public boolean verificarSenha (String senha){
        return this.senha.equals(senha);
    }

    public void depositar (double valor){
        saldo += valor;
    }

    public boolean sacar (double valor){
        if(saldo >= valor){
            saldo -= valor;
            return true;
        } else {
            return false;
        }
    }

    public boolean transferir (Conta destino, double valor){
        if (valor <= saldo){
            saldo -= valor;
            destino.depositar(valor);
            return true;
        } else {
            return false;
        }
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
