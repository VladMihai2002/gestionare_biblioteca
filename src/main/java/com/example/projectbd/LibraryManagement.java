package com.example.projectbd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LibraryManagement {

    private String role;

    public LibraryManagement(String role, JFrame mainMenuFrame) {
        this.role = role;

        // Creare fereastră principală
        JFrame frame = new JFrame("Gestionare Bibliotecă");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 400);

        // Tabel pentru afișarea bibliotecilor
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable libraryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(libraryTable);

        tableModel.addColumn("BibliotecaID");
        tableModel.addColumn("Denumire");
        tableModel.addColumn("Adresa");

        loadLibraries(tableModel);

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
        addButton.addActionListener(e -> addLibrary(tableModel));

        // Funcționalitate pentru modificare
        editButton.addActionListener(e -> {
            int selectedRow = libraryTable.getSelectedRow();
            if (selectedRow != -1) {
                int libraryID = (int) tableModel.getValueAt(selectedRow, 0);
                String denumire = (String) tableModel.getValueAt(selectedRow, 1);
                String adresa = (String) tableModel.getValueAt(selectedRow, 2);
                editLibrary(libraryID, denumire, adresa, tableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Selectați o bibliotecă pentru modificare.");
            }
        });

        // Funcționalitate pentru ștergere
        deleteButton.addActionListener(e -> {
            int selectedRow = libraryTable.getSelectedRow();
            if (selectedRow != -1) {
                int libraryID = (int) tableModel.getValueAt(selectedRow, 0);
                deleteLibrary(libraryID, tableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Selectați o bibliotecă pentru ștergere.");
            }
        });

        // Funcționalitate pentru Înapoi
        backButton.addActionListener(e -> {
            frame.dispose(); // Închidem doar această fereastră
            mainMenuFrame.setVisible(true); // Reafișăm meniul principal
        });
    }

    private void loadLibraries(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String query = "SELECT * FROM Biblioteca";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("BibliotecaID"),
                        rs.getString("Denumire"),
                        rs.getString("Adresa")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addLibrary(DefaultTableModel tableModel) {
        JTextField denumireField = new JTextField();
        JTextField adresaField = new JTextField();

        Object[] fields = {
                "Denumire:", denumireField,
                "Adresă:", adresaField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Adaugă Bibliotecă", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String denumire = denumireField.getText();
            String adresa = adresaField.getText();

            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "INSERT INTO Biblioteca (Denumire, Adresa) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, denumire);
                stmt.setString(2, adresa);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Bibliotecă adăugată cu succes!");
                loadLibraries(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editLibrary(int libraryID, String denumire, String adresa, DefaultTableModel tableModel) {
        JTextField denumireField = new JTextField(denumire);
        JTextField adresaField = new JTextField(adresa);

        Object[] fields = {
                "Denumire:", denumireField,
                "Adresă:", adresaField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Modifică Bibliotecă", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String updatedDenumire = denumireField.getText();
            String updatedAdresa = adresaField.getText();

            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "UPDATE Biblioteca SET Denumire = ?, Adresa = ? WHERE BibliotecaID = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, updatedDenumire);
                stmt.setString(2, updatedAdresa);
                stmt.setInt(3, libraryID);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Bibliotecă modificată cu succes!");
                loadLibraries(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteLibrary(int libraryID, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "Sigur doriți să ștergeți această bibliotecă?", "Confirmare", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "DELETE FROM Biblioteca WHERE BibliotecaID = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, libraryID);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Bibliotecă ștearsă cu succes!");
                loadLibraries(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame mainMenuFrame = new JFrame("Meniu Principal"); // Exemplu
        new LibraryManagement("admin", mainMenuFrame); // Transmitem instanța meniului
    }
}
