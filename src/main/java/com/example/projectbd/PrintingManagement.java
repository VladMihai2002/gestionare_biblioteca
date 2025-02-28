package com.example.projectbd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PrintingManagement {

    public PrintingManagement(String role, JFrame mainMenuFrame) {
        JFrame frame = new JFrame("Gestionare Tipărire");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Închide doar această fereastră
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        // Tabel
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        tableModel.addColumn("CarteID");
        tableModel.addColumn("Denumire Carte");
        tableModel.addColumn("BibliotecaID");
        tableModel.addColumn("Denumire Bibliotecă");
        tableModel.addColumn("Secțiune");

        loadPrintingData(tableModel);

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
            JTextField carteIDField = new JTextField();
            JTextField bibliotecaIDField = new JTextField();
            JTextField sectiuneField = new JTextField();

            Object[] inputFields = {
                    "CarteID:", carteIDField,
                    "BibliotecaID:", bibliotecaIDField,
                    "Secțiune:", sectiuneField
            };

            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Adaugă Tipărire", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String carteID = carteIDField.getText();
                String bibliotecaID = bibliotecaIDField.getText();
                String sectiune = sectiuneField.getText();

                if (!carteID.isEmpty() && !bibliotecaID.isEmpty() && !sectiune.isEmpty()) {
                    addPrinting(carteID, bibliotecaID, sectiune);
                    loadPrintingData(tableModel);
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

            String carteID = table.getValueAt(selectedRow, 0).toString();
            String bibliotecaID = table.getValueAt(selectedRow, 2).toString();
            String currentSectiune = table.getValueAt(selectedRow, 4).toString();

            String newSectiune = JOptionPane.showInputDialog(frame, "Introduceți noua secțiune:", currentSectiune);
            if (newSectiune != null && !newSectiune.trim().isEmpty()) {
                modifyPrinting(carteID, bibliotecaID, newSectiune);
                loadPrintingData(tableModel);
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

            String carteID = table.getValueAt(selectedRow, 0).toString();
            String bibliotecaID = table.getValueAt(selectedRow, 2).toString();

            int option = JOptionPane.showConfirmDialog(frame, "Sunteți sigur că doriți să ștergeți această înregistrare?", "Confirmare Ștergere", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                deletePrinting(carteID, bibliotecaID);
                loadPrintingData(tableModel);
            }
        });

        // Funcționalitatea butonului "Înapoi"
        backButton.addActionListener(e -> {
            frame.dispose(); // Închidem fereastra curentă
            mainMenuFrame.setVisible(true); // Reafișăm meniul principal
        });
    }

    private void loadPrintingData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String query =
                "SELECT " +
                        "   Tiparire.CarteID, " +
                        "   Carti.Denumire AS CarteDenumire, " +
                        "   Tiparire.BibliotecaID, " +
                        "   Biblioteca.Denumire AS BibliotecaDenumire, " +
                        "   Tiparire.Sectiune " +
                        "FROM Tiparire " +
                        "INNER JOIN Carti ON Tiparire.CarteID = Carti.CarteID " +
                        "INNER JOIN Biblioteca ON Tiparire.BibliotecaID = Biblioteca.BibliotecaID;";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("CarteID"),
                        rs.getString("CarteDenumire"),
                        rs.getInt("BibliotecaID"),
                        rs.getString("BibliotecaDenumire"),
                        rs.getString("Sectiune")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPrinting(String carteID, String bibliotecaID, String sectiune) {
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String insertQuery = "INSERT INTO Tiparire (CarteID, BibliotecaID, Sectiune) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, Integer.parseInt(carteID));
            stmt.setInt(2, Integer.parseInt(bibliotecaID));
            stmt.setString(3, sectiune);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Înregistrarea a fost adăugată cu succes.", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Eroare la adăugarea înregistrării: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void modifyPrinting(String carteID, String bibliotecaID, String newSectiune) {
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String updateQuery = "UPDATE Tiparire SET Sectiune = ? WHERE CarteID = ? AND BibliotecaID = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, newSectiune);
            stmt.setInt(2, Integer.parseInt(carteID));
            stmt.setInt(3, Integer.parseInt(bibliotecaID));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Modificarea a fost realizată cu succes.", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Eroare la modificarea înregistrării: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletePrinting(String carteID, String bibliotecaID) {
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String password = "Vlad_2002";

        String deleteQuery = "DELETE FROM Tiparire WHERE CarteID = ? AND BibliotecaID = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, Integer.parseInt(carteID));
            stmt.setInt(2, Integer.parseInt(bibliotecaID));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ștergerea a fost realizată cu succes.", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Eroare la ștergerea înregistrării: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame mainMenuFrame = new JFrame("Meniu Principal"); // Exemplu meniu principal
        new PrintingManagement("admin", mainMenuFrame);
    }
}
