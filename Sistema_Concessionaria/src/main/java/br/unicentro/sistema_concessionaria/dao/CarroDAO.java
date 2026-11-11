package br.unicentro.sistema_concessionaria.dao;

import br.unicentro.sistema_concessionaria.model.Carro;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarroDAO implements ICarro {

    @Override
    public boolean inserir(Carro carro) {
        String sql = "INSERT INTO carro (placa, modelo, marca, ano, preco, imagem) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, carro.getPlaca());
            stmt.setString(2, carro.getModelo());
            stmt.setString(3, carro.getMarca());
            stmt.setInt(4, carro.getAno());
            stmt.setDouble(5, carro.getPreco());
            stmt.setString(6, carro.getImagem());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir carro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean alterar(Carro carro) {
        String sql = "UPDATE carro SET modelo = ?, marca = ?, ano = ?, preco = ?, imagem = ? WHERE placa = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, carro.getModelo());
            stmt.setString(2, carro.getMarca());
            stmt.setInt(3, carro.getAno());
            stmt.setDouble(4, carro.getPreco());
            stmt.setString(5, carro.getImagem());
            stmt.setString(6, carro.getPlaca());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar carro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(String placa) {
        // 1. Buscar o nome do arquivo da imagem antes de apagar do banco
        String caminhoImagem = null;
        String sqlBusca = "SELECT imagem FROM carro WHERE placa = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca)) {
            stmtBusca.setString(1, placa);
            try (ResultSet rs = stmtBusca.executeQuery()) {
                if (rs.next()) {
                    caminhoImagem = rs.getString("imagem");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar imagem para exclusão: " + e.getMessage());
        }

        // 2. Executar o DELETE no banco de dados
        String sqlDelete = "DELETE FROM carro WHERE placa = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {

            stmt.setString(1, placa);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                // 3. Se apagou do banco com sucesso, apaga o arquivo físico da pasta correta
                if (caminhoImagem != null && !caminhoImagem.isEmpty()) {
                    try {
                        File arquivoImagem = new File("imagens_carros/" + caminhoImagem);

                        if (arquivoImagem.exists()) {
                            boolean deletado = arquivoImagem.delete();
                            System.out.println("Arquivo de imagem apagado do disco: " + deletado);
                        }
                    } catch (Exception ex) {
                        System.err.println("Erro ao remover arquivo físico da imagem: " + ex.getMessage());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir carro: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Carro selecionarPorPlaca(String placa) {
        String sql = "SELECT * FROM carro WHERE placa = ?";
        Carro carro = null;
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    carro = new Carro(
                            rs.getString("placa"),
                            rs.getString("modelo"),
                            rs.getString("marca"),
                            rs.getInt("ano"),
                            rs.getDouble("preco"),
                            rs.getString("imagem")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar carro por placa: " + e.getMessage());
        }
        return carro;
    }

    @Override
    public List<Carro> selecionarTodos() {
        String sql = "SELECT * FROM carro";
        List<Carro> carros = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Carro carro = new Carro(
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getString("marca"),
                        rs.getInt("ano"),
                        rs.getDouble("preco"),
                        rs.getString("imagem")
                );
                carros.add(carro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar carros: " + e.getMessage());
        }
        return carros;
    }
}