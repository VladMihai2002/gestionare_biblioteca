package com.example.projectbd;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginScreen {

    public LoginScreen() {
        // Creare fereastră principală
        JFrame frame = new JFrame("Autentificare");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(230, 240, 255)); // Fundal albastru deschis

        // Antet
        JLabel headerLabel = new JLabel("Bine ai venit! Te rugăm să te autentifici.", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(30, 30, 60)); // Text albastru închis
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Panou central
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        centerPanel.setOpaque(false); // Fundal transparent
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel userLabel = new JLabel("Nume utilizator:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Parola:");
        JPasswordField passField = new JPasswordField();

        centerPanel.add(userLabel);
        centerPanel.add(userField);
        centerPanel.add(passLabel);
        centerPanel.add(passField);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Panou pentru buton
        JButton loginButton = new JButton("Conectare");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(100, 149, 237)); // Albastru deschis
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Acțiune buton Conectare
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String validationResult = validateUser(username, password);

            // Afișăm ferestrele de eroare sau succes
            if ("user_not_found".equals(validationResult)) {
                JOptionPane.showMessageDialog(frame, "Utilizatorul nu există.", "Eroare Autentificare", JOptionPane.ERROR_MESSAGE);
            } else if ("wrong_password".equals(validationResult)) {
                JOptionPane.showMessageDialog(frame, "Parola este greșită.", "Eroare Autentificare", JOptionPane.ERROR_MESSAGE);
            } else if ("admin".equals(validationResult) || "client".equals(validationResult)) {
                JOptionPane.showMessageDialog(frame, "Autentificare reușită!", "Succes", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); // Închidem fereastra de login
                new MainMenu(username, validationResult); // Deschidem meniul principal
            } else {
                JOptionPane.showMessageDialog(frame, "Eroare necunoscută. Încercați din nou.", "Eroare Autentificare", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private String validateUser(String username, String password) {
        // Detalii conexiune la baza de date
        String url = "jdbc:postgresql://localhost:5432/ProjectBD_Cretu_Vlad-Mihai";
        String user = "postgres";
        String dbPassword = "Vlad_2002";

        String query = "SELECT Parola, Rol FROM Utilizatori WHERE NumeUtilizator = ?";

        try (Connection conn = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("Parola");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return rs.getString("Rol"); // Returnăm rolul utilizatorului (admin/client)
                } else {
                    return "wrong_password"; // Parola este greșită
                }
            } else {
                return "user_not_found"; // Utilizatorul nu există
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Dacă apare o eroare neașteptată
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
