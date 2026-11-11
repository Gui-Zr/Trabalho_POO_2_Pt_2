package br.unicentro.sistema_concessionaria.controller;

import br.unicentro.sistema_concessionaria.main.GerenciadorTelas;
import br.unicentro.sistema_concessionaria.model.Vendedor;
import br.unicentro.sistema_concessionaria.service.VendedorService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML private ImageView imgLogo;
    @FXML private TextField txtCpf;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    // Substituição do DAO direto pelo VendedorService (Service Layer)
    private final VendedorService vendedorService = new VendedorService();

    // Guarda o vendedor autenticado na sessão atual do sistema
    private static Vendedor vendedorLogado;

    @FXML
    public void initialize() {
        try {
            Image logo = new Image(getClass().getResourceAsStream("/br/unicentro/sistema_concessionaria/image/logo.png"));
            imgLogo.setImage(logo);
        } catch (Exception e) {
            System.out.println("Logo não encontrada na pasta de recursos.");
        }
    }

    @FXML
    public void entrar() {
        String cpf = txtCpf.getText();
        String senha = txtSenha.getText();

        try {
            // Autenticação delegada para a Service Layer (que valida os campos e regra de negócio)
            Vendedor vendedor = vendedorService.autenticar(cpf, senha);

            vendedorLogado = vendedor;
            lblMensagem.setText("");
            // Direciona para o Menu Principal após autenticar com sucesso
            GerenciadorTelas.abreTela("IncluiMenu.fxml", "Sistema de Concessionária - Menu Principal");

        } catch (Exception e) {
            // Exibe a mensagem de erro direto no Label original da tela
            lblMensagem.setText(e.getMessage());
        }
    }

    @FXML
    public void sair() {
        Platform.exit();
    }

    public static Vendedor getVendedorLogado() {
        return vendedorLogado;
    }
}