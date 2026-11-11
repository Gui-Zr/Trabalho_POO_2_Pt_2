package br.unicentro.sistema_concessionaria.controller;

import br.unicentro.sistema_concessionaria.main.GerenciadorTelas;
import javafx.fxml.FXML;

public class MenuController {

    @FXML
    public void abrirCarros() {
        GerenciadorTelas.abreTela("IncluiCarro.fxml", "Sistema de Concessionária - Gerenciamento de Carros");
    }

    @FXML
    public void abrirVendas() {
        GerenciadorTelas.abreTela("IncluiVenda.fxml", "Sistema de Concessionária - Gerenciamento de Vendas");
    }

    @FXML
    public void abrirVendedores() {
        GerenciadorTelas.abreTela("IncluiVendedor.fxml", "Sistema de Concessionária - Gerenciamento de Vendedores");
    }

    @FXML
    public void fazerLogout() {
        GerenciadorTelas.abreTela("Login.fxml", "Sistema de Concessionária - Login");
    }
}