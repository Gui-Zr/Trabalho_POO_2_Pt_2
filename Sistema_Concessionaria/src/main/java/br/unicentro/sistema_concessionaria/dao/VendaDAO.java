package br.unicentro.sistema_concessionaria.dao;

import br.unicentro.sistema_concessionaria.model.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO implements IVenda {

    @Override
    public boolean inserir(Venda venda) {
        String sql = "INSERT INTO venda (placa, cpf, nome_comprador, data_venda, valor_final) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, venda.getPlaca());
            stmt.setString(2, venda.getCpf());
            stmt.setString(3, venda.getNomeComprador());
            stmt.setDate(4, Date.valueOf(venda.getDataVenda()));
            stmt.setDouble(5, venda.getValorFinal());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir venda: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean alterar(Venda venda) {
        String sql = "UPDATE venda SET placa = ?, cpf = ?, nome_comprador = ?, data_venda = ?, valor_final = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, venda.getPlaca());
            stmt.setString(2, venda.getCpf());
            stmt.setString(3, venda.getNomeComprador());
            stmt.setDate(4, Date.valueOf(venda.getDataVenda()));
            stmt.setDouble(5, venda.getValorFinal());
            stmt.setInt(6, venda.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar venda: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(int id) {
        String sql = "DELETE FROM venda WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir venda: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Venda selecionarPorId(int id) {
        String sql = "SELECT * FROM venda WHERE id = ?";
        Venda venda = null;
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    venda = new Venda(
                            rs.getInt("id"),
                            rs.getString("placa"),
                            rs.getString("cpf"),
                            rs.getString("nome_comprador"),
                            rs.getDate("data_venda").toLocalDate(),
                            rs.getDouble("valor_final")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar venda por ID: " + e.getMessage());
        }
        return venda;
    }

    @Override
    public List<Venda> selecionarTodas() {
        String sql = "SELECT * FROM venda";
        List<Venda> vendas = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("cpf"),
                        rs.getString("nome_comprador"),
                        rs.getDate("data_venda").toLocalDate(),
                        rs.getDouble("valor_final")
                );
                vendas.add(venda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas: " + e.getMessage());
        }
        return vendas;
    }
}