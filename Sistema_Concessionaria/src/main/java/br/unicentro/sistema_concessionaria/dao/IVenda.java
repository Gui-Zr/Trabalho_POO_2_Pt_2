package br.unicentro.sistema_concessionaria.dao;

import br.unicentro.sistema_concessionaria.model.Venda;
import java.util.List;

public interface IVenda {
    boolean inserir(Venda venda);
    boolean alterar(Venda venda);
    boolean excluir(int id);
    Venda selecionarPorId(int id);
    List<Venda> selecionarTodas();
}
