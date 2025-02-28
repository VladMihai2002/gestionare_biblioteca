package com.example.projectbd;

import javax.swing.*;
import java.awt.*;

public class MainMenu {

    public MainMenu(String username, String role) {
        JFrame frame = new JFrame("Meniu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(230, 240, 255)); // Fundal albastru deschis

        // Antet
        JLabel headerLabel = new JLabel("Meniu Principal", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(30, 30, 60)); // Text albastru închis
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Panou central pentru butoane
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton autoriButton = createStyledButton("Gestionare Autori");
        JButton cartiButton = createStyledButton("Gestionare Cărți");
        JButton bibliotecaButton = createStyledButton("Gestionare Bibliotecă");
        JButton publicareButton = createStyledButton("Gestionare Publicare");
        JButton tiparireButton = createStyledButton("Gestionare Tipărire");

        buttonPanel.add(autoriButton);
        buttonPanel.add(cartiButton);
        buttonPanel.add(bibliotecaButton);
        buttonPanel.add(publicareButton);
        buttonPanel.add(tiparireButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Buton de logout
        JButton logoutButton = createStyledButton("Deconectare");
        logoutButton.setBackground(new Color(220, 20, 60)); // Roșu
        frame.add(logoutButton, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Acțiuni pentru butoane
        autoriButton.addActionListener(e -> new AuthorManagement(role, frame));
        cartiButton.addActionListener(e -> new BookManagement(role, frame));
        bibliotecaButton.addActionListener(e -> new LibraryManagement(role, frame));
        publicareButton.addActionListener(e -> new PublicationManagement(role, frame));
        tiparireButton.addActionListener(e -> new PrintingManagement(role, frame));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginScreen();
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(100, 149, 237)); // Albastru deschis
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static void main(String[] args) {
        new MainMenu("admin", "admin");
    }
}
