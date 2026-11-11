package br.unicentro.sistema_concessionaria.service;

import br.unicentro.sistema_concessionaria.dao.CarroDAO;
import br.unicentro.sistema_concessionaria.dao.VendaDAO;
import br.unicentro.sistema_concessionaria.model.Carro;
import br.unicentro.sistema_concessionaria.model.Venda;
import br.unicentro.sistema_concessionaria.strategy.CalculoVendaStrategy;
import java.util.List;

public class VendaService {

    private final VendaDAO vendaDAO = new VendaDAO();
    private final CarroDAO carroDAO = new CarroDAO();

    public void registrarVendaComEstrategia(Venda venda, CalculoVendaStrategy estrategia) throws Exception {
        if (venda.getPlaca() == null || venda.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do carro é obrigatória.");
        }
        if (venda.getCpf() == null || venda.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do vendedor é obrigatório.");
        }
        if (venda.getNomeComprador() == null || venda.getNomeComprador().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do comprador é obrigatório.");
        }
        if (venda.getDataVenda() == null) {
            throw new IllegalArgumentException("A data da venda é obrigatória.");
        }

        // Busca o preço base do carro no banco
        Carro carro = carroDAO.selecionarPorPlaca(venda.getPlaca());
        if (carro == null) {
            throw new Exception("Carro não encontrado para a placa informada.");
        }

        // Aplicação do padrão GoF STRATEGY para calcular o valor final
        double valorCalculado = estrategia.calcularValorFinal(carro.getPreco());
        venda.setValorFinal(valorCalculado);

        if (venda.getId() == 0) {
            if (!vendaDAO.inserir(venda)) {
                throw new Exception("Não foi possível registrar a venda.");
            }
        } else {
            if (!vendaDAO.alterar(venda)) {
                throw new Exception("Não foi possível atualizar a venda.");
            }
        }
    }

    public void excluir(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de venda inválido.");
        }
        if (!vendaDAO.excluir(id)) {
            throw new Exception("Não foi possível excluir a venda.");
        }
    }

    public List<Venda> listarTodas() {
        return vendaDAO.selecionarTodas();
    }
}