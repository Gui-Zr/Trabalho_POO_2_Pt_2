package br.unicentro.sistema_concessionaria.dao;

import br.unicentro.sistema_concessionaria.model.Carro;
import java.util.List;

public interface ICarro {
    boolean inserir(Carro carro);
    boolean alterar(Carro carro);
    boolean excluir(String placa);
    Carro selecionarPorPlaca(String placa);
    List<Carro> selecionarTodos();
}
