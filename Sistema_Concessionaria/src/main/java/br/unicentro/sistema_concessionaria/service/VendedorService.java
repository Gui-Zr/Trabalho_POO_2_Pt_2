package br.unicentro.sistema_concessionaria.service;

import br.unicentro.sistema_concessionaria.dao.VendedorDAO;
import br.unicentro.sistema_concessionaria.model.Vendedor;
import java.util.List;

public class VendedorService {

    private final VendedorDAO vendedorDAO = new VendedorDAO();

    public void salvarOuAtualizar(Vendedor vendedor) throws Exception {
        if (vendedor.getCpf() == null || vendedor.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do vendedor é obrigatório.");
        }
        if (vendedor.getNome() == null || vendedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do vendedor é obrigatório.");
        }
        if (vendedor.getSenha() == null || vendedor.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("A senha do vendedor é obrigatória.");
        }

        Vendedor existente = vendedorDAO.selecionarPorCpf(vendedor.getCpf());
        if (existente == null) {
            if (!vendedorDAO.inserir(vendedor)) {
                throw new Exception("Não foi possível cadastrar o vendedor no banco.");
            }
        } else {
            if (!vendedorDAO.alterar(vendedor)) {
                throw new Exception("Não foi possível atualizar o vendedor no banco.");
            }
        }
    }

    public void excluir(String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF inválido para exclusão.");
        }
        if (!vendedorDAO.excluir(cpf)) {
            throw new Exception("Não foi possível excluir o vendedor.");
        }
    }

    public List<Vendedor> listarTodos() {
        return vendedorDAO.selecionarTodos();
    }

    public Vendedor buscarPorCpf(String cpf) {
        return vendedorDAO.selecionarPorCpf(cpf);
    }

    // NOVO: Regra de negócio para autenticação de login
    public Vendedor autenticar(String cpf, String senha) throws Exception {
        if (cpf == null || cpf.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o CPF e a senha para realizar o login.");
        }

        Vendedor vendedor = vendedorDAO.selecionarPorCpf(cpf);
        if (vendedor == null || !vendedor.getSenha().equals(senha)) {
            throw new Exception("CPF ou senha inválidos.");
        }

        return vendedor;
    }
}