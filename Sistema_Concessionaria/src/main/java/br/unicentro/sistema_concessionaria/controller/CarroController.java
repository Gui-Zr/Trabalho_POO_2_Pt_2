package br.unicentro.sistema_concessionaria.controller;

import br.unicentro.sistema_concessionaria.main.GerenciadorTelas;
import br.unicentro.sistema_concessionaria.model.Carro;
import br.unicentro.sistema_concessionaria.service.CarroService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CarroController {

    @FXML private TextField txtPlaca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtMarca;
    @FXML private TextField txtAno;
    @FXML private TextField txtPreco;
    @FXML private Label lblCaminhoImagem;
    @FXML private ImageView imgCarro;
    @FXML private ImageView imgLogoCarros;

    @FXML private TableView<Carro> tabelaCarros;
    @FXML private TableColumn<Carro, String> colPlaca;
    @FXML private TableColumn<Carro, String> colModelo;
    @FXML private TableColumn<Carro, String> colMarca;
    @FXML private TableColumn<Carro, Integer> colAno;
    @FXML private TableColumn<Carro, Double> colPreco;

    // Dependência da Service Layer (preservada)
    private final CarroService carroService = new CarroService();
    private String nomeArquivoImagem = "";

    @FXML
    public void initialize() {
        // Configuração da tabela
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        atualizarTabela();

        try {
            Image logo = new Image(getClass().getResourceAsStream("/br/unicentro/sistema_concessionaria/image/logo.png"));
            imgLogoCarros.setImage(logo);
        } catch (Exception e) {
            // Caso a logo não esteja na pasta, apenas ignora
        }

        // Listener para preencher os campos ao clicar em um item da tabela
        tabelaCarros.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                txtPlaca.setText(novo.getPlaca());
                txtPlaca.setDisable(true); // Placa é chave primária
                txtModelo.setText(novo.getModelo());
                txtMarca.setText(novo.getMarca());
                txtAno.setText(String.valueOf(novo.getAno()));
                txtPreco.setText(String.valueOf(novo.getPreco()));
                nomeArquivoImagem = novo.getImagem();
                lblCaminhoImagem.setText(nomeArquivoImagem);

                if (nomeArquivoImagem != null && !nomeArquivoImagem.isEmpty()) {
                    File file = new File("imagens_carros/" + nomeArquivoImagem);
                    if (file.exists()) {
                        imgCarro.setImage(new Image(file.toURI().toString()));
                    } else {
                        imgCarro.setImage(null);
                    }
                } else {
                    imgCarro.setImage(null);
                }
            }
        });
    }

    @FXML
    public void selecionarImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem do Carro");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        File arquivoSelecionado = fileChooser.showOpenDialog(null);

        if (arquivoSelecionado != null) {
            try {
                File pastaDestino = new File("imagens_carros");
                if (!pastaDestino.exists()) {
                    pastaDestino.mkdirs();
                }

                nomeArquivoImagem = System.currentTimeMillis() + "_" + arquivoSelecionado.getName();
                File arquivoDestino = new File(pastaDestino, nomeArquivoImagem);

                Files.copy(arquivoSelecionado.toPath(), arquivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                lblCaminhoImagem.setText(nomeArquivoImagem);
                imgCarro.setImage(new Image(arquivoDestino.toURI().toString()));
            } catch (IOException e) {
                alerta("Erro", "Erro ao salvar imagem: " + e.getMessage());
            }
        }
    }

    @FXML
    public void salvarCarro() {
        try {
            String placa = txtPlaca.getText();
            String modelo = txtModelo.getText();
            String marca = txtMarca.getText();
            int ano = Integer.parseInt(txtAno.getText());
            double preco = Double.parseDouble(txtPreco.getText());

            // APLICACÃO DO PADRÃO GOF: BUILDER
            Carro carro = new Carro.CarroBuilder()
                    .comPlaca(placa)
                    .comModelo(modelo)
                    .comMarca(marca)
                    .comAno(ano)
                    .comPreco(preco)
                    .comImagem(nomeArquivoImagem)
                    .build();

            // Mantém a Service Layer intacta e integrada
            carroService.salvarOuAtualizar(carro);

            alerta("Sucesso", "Operação realizada com sucesso!");
            limparCampos();
            atualizarTabela();
        } catch (NumberFormatException e) {
            alerta("Erro de Validação", "Verifique se Ano e Preço contêm apenas números válidos.");
        } catch (Exception e) {
            alerta("Erro de Regra de Negócio", e.getMessage());
        }
    }

    @FXML
    public void excluirCarro() {
        Carro selecionado = tabelaCarros.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                carroService.excluir(selecionado.getPlaca());
                alerta("Sucesso", "Carro excluído com sucesso!");
                limparCampos();
                atualizarTabela();
            } catch (Exception e) {
                alerta("Erro ao Excluir", e.getMessage());
            }
        } else {
            alerta("Aviso", "Selecione um carro na tabela para excluir.");
        }
    }

    @FXML
    public void limparCampos() {
        txtPlaca.clear();
        txtPlaca.setDisable(false);
        txtModelo.clear();
        txtMarca.clear();
        txtAno.clear();
        txtPreco.clear();
        lblCaminhoImagem.setText("Nenhuma imagem selecionada");
        imgCarro.setImage(null);
        nomeArquivoImagem = "";
        tabelaCarros.getSelectionModel().clearSelection();
    }

    @FXML
    public void voltarMenu() {
        GerenciadorTelas.voltarMenu();
    }

    private void atualizarTabela() {
        try {
            ObservableList<Carro> lista = FXCollections.observableArrayList(carroService.listarTodos());
            tabelaCarros.setItems(lista);
        } catch (Exception e) {
            alerta("Erro ao carregar dados", "Não foi possível carregar a lista de carros: " + e.getMessage());
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