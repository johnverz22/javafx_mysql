package com.infoman2.controller;

import com.infoman2.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainController {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;

    private DatabaseConnection connection;

    public void initialize(){
        connection = new DatabaseConnection();
    }

    @FXML
    private void save() throws SQLException {
        String sql = "INSERT INTO students(first_name, last_name, email) VALUES(?, ?, ?)";
        PreparedStatement stmt = connection.connection.prepareStatement(sql);
        stmt.setString(1, firstName.getText());
        stmt.setString(2, lastName.getText());
        stmt.setString(3, email.getText());
        stmt.execute();
    }
}
