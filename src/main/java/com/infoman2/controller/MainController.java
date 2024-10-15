package com.infoman2.controller;

import com.infoman2.DatabaseConnection;
import com.infoman2.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TableView<Student> table;
    @FXML
    private TableColumn<Student, String> colFN;
    @FXML
    private TableColumn<Student, String> colLN;
    @FXML
    private TableColumn<Student, String> colEmail;

    private boolean isEditing = false;
    private int studentId = 0;
    private DatabaseConnection db;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void initialize() throws SQLException{
        db = new DatabaseConnection();

        //load and populate our table

        //bind each column to the Student class properties and set value factories
        colFN.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLN.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadStudents();
    }

    public void loadStudents() throws SQLException{
        studentList.clear();
        String sql = "SELECT * from students";

        Statement stmt = db.getConnection().createStatement();
        ResultSet result =  stmt.executeQuery(sql);

        while(result.next()){
            Student student = new Student(result.getInt("id"),
                    result.getString("first_name"),
                    result.getString("last_name"),
                    result.getString("email"));
            studentList.add(student);
        }

        table.setItems(studentList);
    }

    @FXML
    private void save() throws SQLException {
        if(!isEditing) { //if creating new
            String sql = "INSERT INTO students(first_name, last_name, email) VALUES(?, ?, ?)";
            PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setString(1, firstName.getText());
            pstmt.setString(2, lastName.getText());
            pstmt.setString(3, email.getText());
            if (pstmt.executeUpdate() == 1) {
                firstName.clear();
                lastName.clear();
                email.clear();
                loadStudents();
            }
        }else{ //if editing
            String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
            try{
                PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
                pstmt.setString(1, firstName.getText());
                pstmt.setString(2, lastName.getText());
                pstmt.setString(3, email.getText());
                pstmt.setInt(4, studentId);

                if (pstmt.executeUpdate() == 1) {
                    firstName.clear();
                    lastName.clear();
                    email.clear();
                    loadStudents();
                }

                loadStudents();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void delete(){
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if(selectedStudent != null){
            String sql = "DELETE from students WHERE id = ?";
            try{
                PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
                pstmt.setInt(1, selectedStudent.getId());
                pstmt.executeUpdate();

                studentList.remove(selectedStudent);
            }catch(SQLException e){
                e.printStackTrace();
            }

        }
    }
    @FXML
    private void edit(){
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if(selectedStudent != null){
            //fill form with old values
            firstName.setText(selectedStudent.getFirstName());
            lastName.setText(selectedStudent.getLastName());
            email.setText(selectedStudent.getEmail());
            isEditing = true;
            studentId = selectedStudent.getId();
        }
    }
}
