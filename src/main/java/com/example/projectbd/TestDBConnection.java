package com.example.projectbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai"; //numele bazei de date
        String user = "postgres";
        String password = "Vlad_2002"; //parola PostgreSQL

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexiune reușită la PostgreSQL!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
