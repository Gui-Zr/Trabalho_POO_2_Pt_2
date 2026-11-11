package br.unicentro.sistema_concessionaria.dao;

import br.unicentro.sistema_concessionaria.model.Vendedor;
import java.util.List;

public interface IVendedor {
    boolean inserir(Vendedor vendedor);
    boolean alterar(Vendedor vendedor);
    boolean excluir(String cpf);
    Vendedor selecionarPorCpf(String cpf);
    List<Vendedor> selecionarTodos();
    Vendedor autenticar(String cpf, String senha);
}