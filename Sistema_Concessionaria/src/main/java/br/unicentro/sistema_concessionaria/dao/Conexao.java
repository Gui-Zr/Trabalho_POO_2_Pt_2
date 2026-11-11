package br.unicentro.sistema_concessionaria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao implements IConst {

    private static Connection conexao = null;

    public static Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                Class.forName("org.postgresql.Driver");
                conexao = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do PostgreSQL não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
        return conexao;
    }
}
