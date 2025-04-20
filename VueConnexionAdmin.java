package Vue;

import Dao.AdminDAOImpl;
import Dao.DaoFactory;
import Modele.Admin;

import javax.swing.*;
import java.awt.*;

public class VueConnexionAdmin extends JFrame {

    private DaoFactory dao;
    private AdminDAOImpl admindao;

    public VueConnexionAdmin(DaoFactory daoFactory) {
        this.dao = daoFactory;
        this.admindao = new AdminDAOImpl(dao);

        setTitle("Connexion Admin");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField emailField = new JTextField();
        JPasswordField mdpField = new JPasswordField();

        Object[] champs = {
                "Email :", emailField,
                "Mot de passe :", mdpField
        };

        int option = JOptionPane.showConfirmDialog(this, champs, "Connexion Admin", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String mdp = new String(mdpField.getPassword());

            Admin admin = admindao.getAll().stream()
                    .filter(a -> a.getEmail().equals(email) && a.getMotDePasse().equals(mdp))
                    .findFirst()
                    .orElse(null);

            if (admin != null) {
                new VueAdmin(dao);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
                new FenetrePrincipale();
                dispose();
            }
        } else {
            new FenetrePrincipale();
            dispose();
        }
    }
}
