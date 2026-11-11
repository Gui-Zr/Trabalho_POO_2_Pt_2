package br.unicentro.sistema_concessionaria.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GerenciadorTelas {

    private static Stage stagePrincipal;

    public static void setStagePrincipal(Stage stage) {
        stagePrincipal = stage;
    }

    public static void abreTela(String fxmlFile, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(GerenciadorTelas.class.getResource("/br/unicentro/sistema_concessionaria/view/" + fxmlFile));
            Parent root = loader.load();

            if (stagePrincipal == null) {
                stagePrincipal = new Stage();
            }

            stagePrincipal.setTitle(titulo);

            // Define a cena fixando o tamanho padrão de 1600x720
            Scene scene = new Scene(root, 1300, 720);
            stagePrincipal.setScene(scene);

            // Centraliza a janela na tela do monitor
            stagePrincipal.centerOnScreen();
            stagePrincipal.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void voltarMenu() {
        abreTela("IncluiMenu.fxml", "Sistema de Concessionária - Menu Principal");
    }
}