package br.unicentro.sistema_concessionaria.dao;

import br.unicentro.sistema_concessionaria.model.Vendedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO implements IVendedor {

    @Override
    public boolean inserir(Vendedor vendedor) {
        String sql = "INSERT INTO vendedor (cpf, nome, email, senha) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vendedor.getCpf());
            stmt.setString(2, vendedor.getNome());
            stmt.setString(3, vendedor.getEmail());
            stmt.setString(4, vendedor.getSenha());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir vendedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean alterar(Vendedor vendedor) {
        String sql = "UPDATE vendedor SET nome = ?, email = ?, senha = ? WHERE cpf = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vendedor.getNome());
            stmt.setString(2, vendedor.getEmail());
            stmt.setString(3, vendedor.getSenha());
            stmt.setString(4, vendedor.getCpf());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao alterar vendedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(String cpf) {
        String sql = "DELETE FROM vendedor WHERE cpf = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir vendedor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Vendedor selecionarPorCpf(String cpf) {
        String sql = "SELECT * FROM vendedor WHERE cpf = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vendedor(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao selecionar vendedor por CPF: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Vendedor> selecionarTodos() {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM vendedor";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vendedores.add(new Vendedor(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar vendedores: " + e.getMessage());
        }
        return vendedores;
    }

    // Método novo para validar o Login
    public Vendedor autenticar(String cpf, String senha) {
        String sql = "SELECT * FROM vendedor WHERE cpf = ? AND senha = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vendedor(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar vendedor: " + e.getMessage());
        }
        return null; // Retorna null se não encontrar ou senha estiver errada
    }
}