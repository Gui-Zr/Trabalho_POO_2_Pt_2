module br.unicentro.sistema_concessionaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens br.unicentro.sistema_concessionaria.controller to javafx.fxml;

    opens br.unicentro.sistema_concessionaria.model to javafx.base;

    exports br.unicentro.sistema_concessionaria.main;
    exports br.unicentro.sistema_concessionaria.controller;
    exports br.unicentro.sistema_concessionaria.model;
    exports br.unicentro.sistema_concessionaria.dao;
}