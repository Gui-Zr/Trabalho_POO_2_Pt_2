package br.unicentro.sistema_concessionaria.model;

import java.time.LocalDate;

public class Venda {
    private int id;
    private String placa;
    private String cpf;
    private String nomeComprador;
    private LocalDate dataVenda;
    private double valorFinal; // Novo campo para o Strategy

    public Venda() {
    }

    public Venda(int id, String placa, String cpf, String nomeComprador, LocalDate dataVenda, double valorFinal) {
        this.id = id;
        this.placa = placa;
        this.cpf = cpf;
        this.nomeComprador = nomeComprador;
        this.dataVenda = dataVenda;
        this.valorFinal = valorFinal;
    }

    public Venda(int id, String placa, String cpf, String nomeComprador, LocalDate dataVenda) {
        this.id = id;
        this.placa = placa;
        this.cpf = cpf;
        this.nomeComprador = nomeComprador;
        this.dataVenda = dataVenda;
        this.valorFinal = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }
}