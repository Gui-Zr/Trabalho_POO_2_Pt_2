package br.unicentro.sistema_concessionaria.controller;

import br.unicentro.sistema_concessionaria.main.GerenciadorTelas;
import br.unicentro.sistema_concessionaria.model.Vendedor;
import br.unicentro.sistema_concessionaria.service.VendedorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VendedorController {

    @FXML private TextField txtCpf;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ImageView imgLogoVendedores;

    @FXML private TableView<Vendedor> tabelaVendedores;
    @FXML private TableColumn<Vendedor, String> colCpf;
    @FXML private TableColumn<Vendedor, String> colNome;
    @FXML private TableColumn<Vendedor, String> colEmail;

    // Dependência da Service Layer (substitui o VendedorDAO direto)
    private final VendedorService vendedorService = new VendedorService();

    @FXML
    public void initialize() {
        // Configura as colunas da tabela
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Carrega a logo se o componente estiver presente
        if (imgLogoVendedores != null) {
            try {
                Image logo = new Image(getClass().getResourceAsStream("/br/unicentro/sistema_concessionaria/image/logo.png"));
                imgLogoVendedores.setImage(logo);
            } catch (Exception e) {
                // Silencioso caso a imagem não exista no caminho
            }
        }

        atualizarTabela();

        // Listener para preencher os campos ao selecionar uma linha na tabela
        tabelaVendedores.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                txtCpf.setText(novo.getCpf());
                txtCpf.setDisable(true); // CPF é chave primária
                txtNome.setText(novo.getNome());
                txtEmail.setText(novo.getEmail());
                txtSenha.setText(novo.getSenha());
            }
        });
    }

    @FXML
    public void salvarVendedor() {
        try {
            String cpf = txtCpf.getText();
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String senha = txtSenha.getText();

            Vendedor vendedor = new Vendedor(cpf, nome, email, senha);

            // Delega a lógica de validação e verificação de inserção/alteração para a Service Layer
            vendedorService.salvarOuAtualizar(vendedor);

            alerta("Sucesso", "Operação realizada com sucesso!");
            limparCampos();
            atualizarTabela();
        } catch (Exception e) {
            alerta("Erro de Validação / Negócio", e.getMessage());
        }
    }

    @FXML
    public void excluirVendedor() {
        Vendedor selecionado = tabelaVendedores.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                vendedorService.excluir(selecionado.getCpf());
                alerta("Sucesso", "Vendedor excluído com sucesso!");
                limparCampos();
                atualizarTabela();
            } catch (Exception e) {
                alerta("Erro", "Não foi possível excluir o vendedor: " + e.getMessage());
            }
        } else {
            alerta("Aviso", "Selecione um vendedor na tabela para excluir.");
        }
    }

    @FXML
    public void limparCampos() {
        txtCpf.clear();
        txtCpf.setDisable(false);
        txtNome.clear();
        txtEmail.clear();
        txtSenha.clear();
        tabelaVendedores.getSelectionModel().clearSelection();
    }

    @FXML
    public void voltarMenu() {
        GerenciadorTelas.voltarMenu();
    }

    private void atualizarTabela() {
        try {
            ObservableList<Vendedor> lista = FXCollections.observableArrayList(vendedorService.listarTodos());
            tabelaVendedores.setItems(lista);
        } catch (Exception e) {
            alerta("Erro", "Não foi possível carregar a lista de vendedores: " + e.getMessage());
        }
    }

    private void alerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}