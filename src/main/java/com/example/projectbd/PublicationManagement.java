package com.example.projectbd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PublicationManagement {

    public PublicationManagement(String role, JFrame mainMenuFrame) {
        JFrame frame = new JFrame("Gestionare Publicare");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Închide doar această fereastră
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        // Tabel
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        tableModel.addColumn("AutorID");
        tableModel.addColumn("Nume Autor");
        tableModel.addColumn("Prenume Autor");
        tableModel.addColumn("CarteID");
        tableModel.addColumn("Denumire Carte");
        tableModel.addColumn("Tip");

        loadPublicationData(tableModel);

        // Butoane
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Adaugă");
        JButton modifyButton = new JButton("Modifică");
        JButton deleteButton = new JButton("Șterge");
        JButton backButton = new JButton("Înapoi");

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Dezactivează butonul de ștergere pentru utilizatorii cu rolul de client
        if (role.equals("client")) {
            deleteButton.setEnabled(false); // Dezactivăm butonul pentru client
        }

        // Funcționalitatea butonului "Adaugă"
        addButton.addActionListener(e -> {
            JTextField autorIDField = new JTextField();
            JTextField carteIDField = new JTextField();
            JTextField tipField = new JTextField();

            Object[] inputFields = {
                    "AutorID:", autorIDField,
                    "CarteID:", carteIDField,
                    "Tip:", tipField
            };

            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Adaugă Publicare", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String autorID = autorIDField.getText();
                String carteID = carteIDField.getText();
                String tip = tipField.getText();

                if (!autorID.isEmpty() && !carteID.isEmpty() && !tip.isEmpty()) {
                    addPublication(autorID, carteID, tip);
                    loadPublicationData(tableModel);
                } else {
                    JOptionPane.showMessageDialog(frame, "Toate câmpurile sunt obligatorii!", "Eroare", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Funcționalitatea butonului "Modifică"
        modifyButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Selectați o linie pentru modificare.", "Eroare", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String autorID = table.getValueAt(selectedRow, 0).toString();
            String carteID = table.getValueAt(selectedRow, 3).toString();
            String currentTip = table.getValueAt(selectedRow, 5).toString();

            String newTip = JOptionPane.showInputDialog(frame, "Introduceți noul tip:", currentTip);
            if (newTip != null && !newTip.trim().isEmpty()) {
                modifyPublication(autorID, carteID, newTip);
                loadPublicationData(tableModel);
            }
        });

        // Funcționalitatea butonului "Șterge"
        deleteButton.addActionListener(e -> {
            if (role.equals("client")) {
                JOptionPane.showMessageDialog(frame, "Clienții nu au permisiunea de a șterge date.", "Acces restricționat", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Selectați o linie pentru ștergere.", "Eroare", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String autorID = table.getValueAt(selectedRow, 0).toString();
            String carteID = table.getValueAt(selectedRow, 3).toString();

            int option = JOptionPane.showConfirmDialog(frame, "Sunteți sigur că doriți să ștergeți această înregistrare?", "Confirmare Ștergere", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                deletePublication(autorID, carteID);
                loadPublicationData(tableModel);
            }
        });

        // Funcționalitatea butonului "Înapoi"
        backButton.addActionListener(e -> {
            frame.dispose(); // Închidem fereastra curentă
            mainMenuFrame.setVisible(true); // Reafișăm meniul principal
        });
    }

    private void loadPublicationData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String query =
                "SELECT " +
                        "   Publicare.AutorID, " +
                        "   Autori.NumeAutor, " +
                        "   Autori.PrenumeAutor, " +
                        "   Publicare.CarteID, " +
                        "   Carti.Denumire, " +
                        "   Publicare.Tip " +
                        "FROM Publicare " +
                        "INNER JOIN Autori ON Publicare.AutorID = Autori.AutorID " +
                        "INNER JOIN Carti ON Publicare.CarteID = Carti.CarteID;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("AutorID"),
                        rs.getString("NumeAutor"),
                        rs.getString("PrenumeAutor"),
                        rs.getInt("CarteID"),
                        rs.getString("Denumire"),
                        rs.getString("Tip")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPublication(String autorID, String carteID, String tip) {
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String insertQuery = "INSERT INTO Publicare (AutorID, CarteID, Tip) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, Integer.parseInt(autorID));
            stmt.setInt(2, Integer.parseInt(carteID));
            stmt.setString(3, tip);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Înregistrarea a fost adăugată cu succes.", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Eroare la adăugarea înregistrării: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void modifyPublication(String autorID, String carteID, String newTip) {
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String updateQuery = "UPDATE Publicare SET Tip = ? WHERE AutorID = ? AND CarteID = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, newTip);
            stmt.setInt(2, Integer.parseInt(autorID));
            stmt.setInt(3, Integer.parseInt(carteID));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modificarea a fost realizată cu succes.", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Eroare la modificarea înregistrării: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletePublication(String autorID, String carteID) {
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String deleteQuery = "DELETE FROM Publicare WHERE AutorID = ? AND CarteID = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, Integer.parseInt(autorID));
            stmt.setInt(2, Integer.parseInt(carteID));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ștergerea a fost realizată cu succes.", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Eroare la ștergerea înregistrării: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame mainMenuFrame = new JFrame("Meniu Principal"); // Exemplu meniu principal
        new PublicationManagement("admin", mainMenuFrame);
    }
}
