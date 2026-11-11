package br.unicentro.sistema_concessionaria.controller;

import br.unicentro.sistema_concessionaria.facade.ConcessionariaFacade;
import br.unicentro.sistema_concessionaria.main.GerenciadorTelas;
import br.unicentro.sistema_concessionaria.model.Carro;
import br.unicentro.sistema_concessionaria.model.Venda;
import br.unicentro.sistema_concessionaria.model.Vendedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class VendaController {

    @FXML private ComboBox<String> cbPlacaCarro;
    @FXML private ComboBox<String> cbTipoPagamento;
    @FXML private Label lblVendedorLogado;
    @FXML private TextField txtNomeComprador;
    @FXML private DatePicker dpDataVenda;
    @FXML private TextField txtBusca;
    @FXML private ImageView imgLogoVendas;

    @FXML private TableView<Venda> tabelaVendas;
    @FXML private TableColumn<Venda, Integer> colId;
    @FXML private TableColumn<Venda, String> colPlaca;
    @FXML private TableColumn<Venda, String> colCpfVendedor;
    @FXML private TableColumn<Venda, String> colComprador;
    @FXML private TableColumn<Venda, LocalDate> colData;
    @FXML private TableColumn<Venda, Double> colValorFinal;

    // APLICAÇÃO DO PADRÃO GOF: FACADE (Centraliza o acesso aos subsistemas)
    private final ConcessionariaFacade concessionariaFacade = new ConcessionariaFacade();

    private ObservableList<Venda> listaVendas;
    private Venda vendaSelecionada = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colCpfVendedor.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colComprador.setCellValueFactory(new PropertyValueFactory<>("nomeComprador"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));

        if (colValorFinal != null) {
            colValorFinal.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        }

        if (cbTipoPagamento != null) {
            cbTipoPagamento.setItems(FXCollections.observableArrayList("À Vista (5% Desconto)", "Financiado (+3%)", "Normal"));
            cbTipoPagamento.setValue("Normal");
        }

        if (imgLogoVendas != null) {
            try {
                Image logo = new Image(getClass().getResourceAsStream("/br/unicentro/sistema_concessionaria/image/logo.png"));
                imgLogoVendas.setImage(logo);
            } catch (Exception e) {
                System.out.println("Logo não encontrada.");
            }
        }

        Vendedor logado = LoginController.getVendedorLogado();
        if (logado != null) {
            lblVendedorLogado.setText(logado.getNome() + " (CPF: " + logado.getCpf() + ")");
        } else {
            lblVendedorLogado.setText("Nenhum vendedor autenticado");
        }

        carregarPlacasCarros();
        atualizarTabela();

        tabelaVendas.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                vendaSelecionada = novo;
                cbPlacaCarro.setValue(novo.getPlaca());
                txtNomeComprador.setText(novo.getNomeComprador());
                dpDataVenda.setValue(novo.getDataVenda());
            }
        });
    }

    private void carregarPlacasCarros() {
        try {
            ObservableList<String> placas = FXCollections.observableArrayList();
            for (Carro c : concessionariaFacade.listarTodosCarros()) {
                placas.add(c.getPlaca());
            }
            cbPlacaCarro.setItems(placas);
        } catch (Exception e) {
            alerta("Erro", "Erro ao carregar placas de carros: " + e.getMessage());
        }
    }

    @FXML
    public void salvarVenda() {
        try {
            String placa = cbPlacaCarro.getValue();
            String comprador = txtNomeComprador.getText();
            LocalDate data = dpDataVenda.getValue();
            Vendedor logado = LoginController.getVendedorLogado();

            if (logado == null) {
                alerta("Erro", "Nenhum vendedor autenticado no sistema.");
                return;
            }

            String formaPagamento = (cbTipoPagamento != null && cbTipoPagamento.getValue() != null)
                    ? cbTipoPagamento.getValue() : "Normal";

            if (vendaSelecionada == null) {
                Venda novaVenda = new Venda(0, placa, logado.getCpf(), comprador, data, 0.0);

                // Chamada via FACADE
                concessionariaFacade.registrarOuAtualizarVenda(novaVenda, formaPagamento);

                alerta("Sucesso", "Venda registrada com sucesso! Valor final: R$ " + String.format("%.2f", novaVenda.getValorFinal()));
            } else {
                vendaSelecionada.setPlaca(placa);
                vendaSelecionada.setCpf(logado.getCpf());
                vendaSelecionada.setNomeComprador(comprador);
                vendaSelecionada.setDataVenda(data);

                // Chamada via FACADE
                concessionariaFacade.registrarOuAtualizarVenda(vendaSelecionada, formaPagamento);

                alerta("Sucesso", "Venda atualizada com sucesso! Valor final: R$ " + String.format("%.2f", vendaSelecionada.getValorFinal()));
            }

            limparCampos();
            atualizarTabela();
        } catch (Exception e) {
            alerta("Erro de Regra de Negócio", e.getMessage());
        }
    }

    @FXML
    public void excluirVenda() {
        if (vendaSelecionada != null) {
            try {
                concessionariaFacade.excluirVenda(vendaSelecionada.getId());
                alerta("Sucesso", "Venda excluída com sucesso!");
                limparCampos();
                atualizarTabela();
            } catch (Exception e) {
                alerta("Erro", "Não foi possível excluir a venda: " + e.getMessage());
            }
        } else {
            alerta("Aviso", "Selecione uma venda na tabela para excluir.");
        }
    }

    @FXML
    public void limparCampos() {
        cbPlacaCarro.setValue(null);
        if (cbTipoPagamento != null) cbTipoPagamento.setValue("Normal");
        txtNomeComprador.clear();
        dpDataVenda.setValue(null);
        vendaSelecionada = null;
        tabelaVendas.getSelectionModel().clearSelection();
    }

    @FXML
    public void voltarMenu() {
        GerenciadorTelas.voltarMenu();
    }

    private void atualizarTabela() {
        try {
            listaVendas = FXCollections.observableArrayList(concessionariaFacade.listarTodasVendas());

            FilteredList<Venda> dadosFiltrados = new FilteredList<>(listaVendas, b -> true);

            if (txtBusca != null) {
                txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
                    dadosFiltrados.setPredicate(venda -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (venda.getPlaca() != null && venda.getPlaca().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (venda.getNomeComprador() != null && venda.getNomeComprador().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (venda.getCpf() != null && venda.getCpf().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (String.valueOf(venda.getId()).contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });
                });
            }

            SortedList<Venda> dadosOrdenados = new SortedList<>(dadosFiltrados);
            dadosOrdenados.comparatorProperty().bind(tabelaVendas.comparatorProperty());
            tabelaVendas.setItems(dadosOrdenados);
        } catch (Exception e) {
            alerta("Erro", "Não foi possível carregar a lista de vendas: " + e.getMessage());
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