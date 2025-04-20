package Vue;

import Dao.DaoFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetrePrincipale extends JFrame {
    DaoFactory dao = DaoFactory.getInstance("shopping", "root", "");

    public FenetrePrincipale() {
        setTitle("Shopping App - Menu Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // centre la fenêtre

        // === Titre
        JLabel titre = new JLabel("Bienvenue sur l'application Shopping");
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 16));

        // === Boutons
        JButton btnClient = new JButton("Accès Client");
        JButton btnAdmin = new JButton("Accès Admin");
        JButton btnQuitter = new JButton("Quitter");

        // === Listeners
        btnClient.addActionListener(e -> {
            new VueAccueilClient(DaoFactory.getInstance("shopping", "root", ""));
            dispose();
        });

        btnAdmin.addActionListener(e -> {
            new VueConnexionAdmin(DaoFactory.getInstance("shopping", "root", ""));
            dispose();
        });



        btnQuitter.addActionListener(e -> System.exit(0));

        // === Mise en page
        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new GridLayout(3, 1, 10, 10));
        panelBoutons.add(btnClient);
        panelBoutons.add(btnAdmin);
        panelBoutons.add(btnQuitter);

        JPanel contenu = new JPanel(new BorderLayout(10, 10));
        contenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contenu.add(titre, BorderLayout.NORTH);
        contenu.add(panelBoutons, BorderLayout.CENTER);

        add(contenu);
        setVisible(true);
    }

    // === Point d'entrée
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FenetrePrincipale::new);
    }
}
