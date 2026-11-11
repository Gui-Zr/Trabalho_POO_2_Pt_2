package br.unicentro.sistema_concessionaria.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GerenciadorTelas.setStagePrincipal(primaryStage);
        GerenciadorTelas.abreTela("Login.fxml", "Sistema de Concessionária - Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}