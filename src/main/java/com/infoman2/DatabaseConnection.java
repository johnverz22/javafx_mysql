package com.infoman2;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private String url = "jdbc:mysql://localhost:3306/infoman2_fxapp_db";
    private String user = "app";
    private String password = "1234";
    public Connection connection;

    public DatabaseConnection(){
        try{
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected successfully");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
