package Vue;

import Dao.ClientDAOImpl;
import Dao.DaoFactory;
import Modele.Client;

import javax.swing.*;
import java.awt.*;

public class VueAccueilClient extends JFrame {
    private DaoFactory dao;
    private ClientDAOImpl clidao;

    public VueAccueilClient(DaoFactory daoFactory) {
        this.dao = daoFactory;
        this.clidao = new ClientDAOImpl(dao);

        setTitle("Espace Client - Connexion ou Inscription");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnConnexion = new JButton("Connexion");
        JButton btnInscription = new JButton("Inscription");

        btnConnexion.addActionListener(e -> ouvrirConnexion());
        btnInscription.addActionListener(e -> ouvrirInscription());

        JPanel content = new JPanel(new GridLayout(2, 1, 20, 20));
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        content.add(btnConnexion);
        content.add(btnInscription);

        setContentPane(content);
        setVisible(true);
    }

    private void ouvrirInscription() {
        JTextField nomField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField mdpField = new JPasswordField();

        Object[] champs = {
                "Nom :", nomField,
                "Email :", emailField,
                "Mot de passe :", mdpField
        };

        int option = JOptionPane.showConfirmDialog(this, champs, "Inscription", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nom = nomField.getText();
            String email = emailField.getText();
            String mdp = new String(mdpField.getPassword());

            Client nouveau = new Client(0, nom, email, mdp);
            clidao.ajouter(nouveau);

            // Récupère l'ID généré
            Client recup = clidao.getAll().stream()
                    .filter(c -> c.getEmail().equals(email))
                    .findFirst().orElse(null);

            if (recup != null) {
                new VueClient(dao, recup);
                dispose();
            }
        }
    }

    private void ouvrirConnexion() {
        JTextField emailField = new JTextField();
        JPasswordField mdpField = new JPasswordField();

        Object[] champs = {
                "Email :", emailField,
                "Mot de passe :", mdpField
        };

        int option = JOptionPane.showConfirmDialog(this, champs, "Connexion", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String mdp = new String(mdpField.getPassword());

            Client connecte = clidao.getAll().stream()
                    .filter(c -> c.getEmail().equals(email) && c.getMotDePasse().equals(mdp))
                    .findFirst().orElse(null);

            if (connecte != null) {
                new VueClient(dao, connecte);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
