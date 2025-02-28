package com.example.projectbd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BookManagement {

    private String role;

    public BookManagement(String role, JFrame mainMenuFrame) {
        this.role = role;

        // Creare fereastră principală
        JFrame frame = new JFrame("Gestionare Cărți");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 400);

        // Tabel pentru afișarea cărților
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        tableModel.addColumn("CarteID");
        tableModel.addColumn("Denumire");
        tableModel.addColumn("AnAparitie");
        tableModel.addColumn("Editura");

        loadBooks(tableModel);

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
        addButton.addActionListener(e -> addBook(tableModel));

        // Funcționalitate pentru modificare
        editButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookID = (int) tableModel.getValueAt(selectedRow, 0);
                String denumire = (String) tableModel.getValueAt(selectedRow, 1);
                int anAparitie = (int) tableModel.getValueAt(selectedRow, 2);
                String editura = (String) tableModel.getValueAt(selectedRow, 3);
                editBook(bookID, denumire, anAparitie, editura, tableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Selectați o carte pentru modificare.");
            }
        });

        // Funcționalitate pentru ștergere
        deleteButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookID = (int) tableModel.getValueAt(selectedRow, 0);
                deleteBook(bookID, tableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Selectați o carte pentru ștergere.");
            }
        });

        // Funcționalitate pentru Înapoi
        backButton.addActionListener(e -> {
            frame.dispose(); // Închidem doar această fereastră
            mainMenuFrame.setVisible(true); // Reafișăm meniul principal
        });
    }

    private void loadBooks(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String query = "SELECT * FROM Carti";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("CarteID"),
                        rs.getString("Denumire"),
                        rs.getInt("AnAparitie"),
                        rs.getString("Editura")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBook(DefaultTableModel tableModel) {
        JTextField denumireField = new JTextField();
        JTextField anField = new JTextField();
        JTextField edituraField = new JTextField();

        Object[] fields = {
                "Denumire:", denumireField,
                "An Apatiție:", anField,
                "Editură:", edituraField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Adaugă Carte", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String denumire = denumireField.getText();
            int anAparitie = Integer.parseInt(anField.getText());
            String editura = edituraField.getText();

            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "INSERT INTO Carti (Denumire, AnAparitie, Editura) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, denumire);
                stmt.setInt(2, anAparitie);
                stmt.setString(3, editura);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Carte adăugată cu succes!");
                loadBooks(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editBook(int bookID, String denumire, int anAparitie, String editura, DefaultTableModel tableModel) {
        JTextField denumireField = new JTextField(denumire);
        JTextField anField = new JTextField(String.valueOf(anAparitie));
        JTextField edituraField = new JTextField(editura);

        Object[] fields = {
                "Denumire:", denumireField,
                "An Apariție:", anField,
                "Editură:", edituraField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Modifică Carte", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String updatedDenumire = denumireField.getText();
            int updatedAn = Integer.parseInt(anField.getText());
            String updatedEditura = edituraField.getText();

            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "UPDATE Carti SET Denumire = ?, AnAparitie = ?, Editura = ? WHERE CarteID = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, updatedDenumire);
                stmt.setInt(2, updatedAn);
                stmt.setString(3, updatedEditura);
                stmt.setInt(4, bookID);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Carte modificată cu succes!");
                loadBooks(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteBook(int bookID, DefaultTableModel tableModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "Sigur doriți să ștergeți această carte?", "Confirmare", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
            String user = "postgres";
            String password = "Vlad_2002";

            String query = "DELETE FROM Carti WHERE CarteID = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, bookID);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Carte ștearsă cu succes!");
                loadBooks(tableModel);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame mainMenuFrame = new JFrame("Meniu Principal"); // Exemplu
        new BookManagement("admin", mainMenuFrame); // Transmitem instanța meniului
    }
}
