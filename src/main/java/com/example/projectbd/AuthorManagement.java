package com.example.projectbd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AuthorManagement {

    private String role;

    public AuthorManagement(String role, JFrame mainMenuFrame) {
        this.role = role;

        // Creare fereastră principală
        JFrame frame = new JFrame("Gestionare Autori");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        // Tabel pentru afișarea autorilor
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable authorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(authorTable);

        tableModel.addColumn("AutorID");
        tableModel.addColumn("NumeAutor");
        tableModel.addColumn("PrenumeAutor");
        tableModel.addColumn("TaraOrigine");

        loadAuthors(tableModel);

        // Butoane pentru operații CRUD
        JButton addButton = new JButton("Adaugă");
        JButton editButton = new JButton("Modifică");
        JButton deleteButton = new JButton("Șterge");
        JButton backButton = new JButton("Înapoi"); // Adăugăm butonul Înapoi

        if (!role.equals("admin")) {
            deleteButton.setEnabled(false); // Dezactivăm butonul de ștergere pentru clienți
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton); // Adăugăm butonul Înapoi

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Funcționalitate pentru adăugare
        addButton.addActionListener(e -> addAuthor(tableModel));

        // Funcționalitate pentru modificare
        editButton.addActionListener(e -> {
            int selectedRow = authorTable.getSelectedRow();
            if (selectedRow != -1) {
                int authorID = (int) tableModel.getValueAt(selectedRow, 0);
                String nume = (String) tableModel.getValueAt(selectedRow, 1);
                String prenume = (String) tableModel.getValueAt(selectedRow, 2);
                String tara = (String) tableModel.getValueAt(selectedRow, 3);
                editAuthor(authorID, nume, prenume, tara, tableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Selectați un autor pentru modificare.");
            }
        });

        // Funcționalitate pentru ștergere
        deleteButton.addActionListener(e -> {
            int selectedRow = authorTable.getSelectedRow();
            if (selectedRow != -1) {
                int authorID = (int) tableModel.getValueAt(selectedRow, 0);
                deleteAuthor(authorID, tableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Selectați un autor pentru ștergere.");
            }
        });

        // Funcționalitate pentru Înapoi
        backButton.addActionListener(e -> {
            frame.dispose(); // Închidem doar această fereastră
            mainMenuFrame.setVisible(true); // Reafișăm meniul principal
        });
    }

    private void loadAuthors(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String query = "SELECT * FROM Autori";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("AutorID"),
                        rs.getString("NumeAutor"),
                        rs.getString("PrenumeAutor"),
                        rs.getString("TaraOrigine")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAuthor(DefaultTableModel tableModel) {
        JTextField numeField = new JTextField();
        JTextField prenumeField = new JTextField();
        JTextField taraField = new JTextField();

        Object[] fields = {
                "Nume:", numeField,
                "Prenume:", prenumeField,
                "Țara de origine:", taraField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Adaugă Autor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String nume = numeField.getText();
            String prenume = prenumeField.getText();
            String tara = taraField.getText();

            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "INSERT INTO Autori (NumeAutor, PrenumeAutor, TaraOrigine) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, nume);
                stmt.setString(2, prenume);
                stmt.setString(3, tara);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Autor adăugat cu succes!");
                loadAuthors(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editAuthor(int authorID, String nume, String prenume, String tara, DefaultTableModel tableModel) {
        JTextField numeField = new JTextField(nume);
        JTextField prenumeField = new JTextField(prenume);
        JTextField taraField = new JTextField(tara);

        Object[] fields = {
                "Nume:", numeField,
                "Prenume:", prenumeField,
                "Țara de origine:", taraField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Modifică Autor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String updatedNume = numeField.getText();
            String updatedPrenume = prenumeField.getText();
            String updatedTara = taraField.getText();

            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "UPDATE Autori SET NumeAutor = ?, PrenumeAutor = ?, TaraOrigine = ? WHERE AutorID = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, updatedNume);
                stmt.setString(2, updatedPrenume);
                stmt.setString(3, updatedTara);
                stmt.setInt(4, authorID);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Autor modificat cu succes!");
                loadAuthors(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteAuthor(int authorID, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "Sigur doriți să ștergeți acest autor?", "Confirmare", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "DELETE FROM Autori WHERE AutorID = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, authorID);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Autor șters cu succes!");
                loadAuthors(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame mainMenuFrame = new JFrame("Meniu Principal"); // Exemplu
        new AuthorManagement("admin", mainMenuFrame); // Transmitem instanța meniului
    }
}
