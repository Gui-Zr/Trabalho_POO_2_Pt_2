package br.unicentro.sistema_concessionaria.service;

import br.unicentro.sistema_concessionaria.dao.CarroDAO;
import br.unicentro.sistema_concessionaria.model.Carro;
import java.util.List;

public class CarroService {

    private final CarroDAO carroDAO = new CarroDAO();

    public void salvarOuAtualizar(Carro carro) throws Exception {
        if (carro.getPlaca() == null || carro.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do carro é obrigatória.");
        }
        if (carro.getModelo() == null || carro.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo do carro é obrigatório.");
        }
        if (carro.getMarca() == null || carro.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("A marca do carro é obrigatória.");
        }
        if (carro.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do carro deve ser maior que zero.");
        }

        // Verifica se o carro já existe no banco pela placa
        Carro existente = carroDAO.selecionarPorPlaca(carro.getPlaca());
        if (existente == null) {
            if (!carroDAO.inserir(carro)) {
                throw new Exception("Não foi possível cadastrar o carro no banco.");
            }
        } else {
            if (!carroDAO.alterar(carro)) {
                throw new Exception("Não foi possível atualizar o carro no banco.");
            }
        }
    }

    public void excluir(String placa) throws Exception {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Placa inválida para exclusão.");
        }
        if (!carroDAO.excluir(placa)) {
            throw new Exception("Não foi possível excluir o carro.");
        }
    }

    public List<Carro> listarTodos() {
        return carroDAO.selecionarTodos();
    }

    public Carro buscarPorPlaca(String placa) {
        return carroDAO.selecionarPorPlaca(placa);
    }
}