package Vue;

import Dao.*;
import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VueAdmin extends JFrame {

    private DaoFactory dao;
    private ArticleDAOImpl adao;
    private ClientDAOImpl clidao;
    private Statistiques stats;

    public VueAdmin(DaoFactory daoFactory) {
        this.dao = daoFactory;
        this.adao = new ArticleDAOImpl(dao);
        this.clidao = new ClientDAOImpl(dao);
        this.stats = new Statistiques(dao);

        setTitle("Espace Administrateur");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnArticles = new JButton("Gérer les articles");
        JButton btnClients = new JButton("Gérer les clients");
        JButton btnStats = new JButton("Voir les statistiques");
        JButton btnRetour = new JButton("⬅️ Retour");

        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.add(btnArticles);
        panel.add(btnClients);
        panel.add(btnStats);
        panel.add(btnRetour);

        setContentPane(panel);

        // === Actions ===
        btnArticles.addActionListener(e -> gererArticles());
        btnClients.addActionListener(e -> gererClients());
        btnStats.addActionListener(e -> afficherStatistiques());
        btnRetour.addActionListener(e -> {
            new FenetrePrincipale();
            dispose();
        });

        setVisible(true);
    }

    private void gererArticles() {
        String[] options = {"Ajouter", "Modifier", "Supprimer"};
        int choix = JOptionPane.showOptionDialog(this, "Choisissez une action", "Articles",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choix == 0) {
            // Ajouter
            JTextField nom = new JTextField();
            JTextField desc = new JTextField();
            JTextField prix = new JTextField();
            JTextField prixGros = new JTextField();
            JTextField seuilGros = new JTextField();
            JTextField stock = new JTextField();
            JTextField idMarque = new JTextField();

            Object[] form = {
                    "Nom :", nom,
                    "Description :", desc,
                    "Prix unitaire :", prix,
                    "Prix gros :", prixGros,
                    "Seuil gros :", seuilGros,
                    "Stock :", stock,
                    "ID Marque :", idMarque
            };

            int res = JOptionPane.showConfirmDialog(this, form, "Ajouter un article", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                Marque marque = new MarqueDAOImpl(dao).chercher(Integer.parseInt(idMarque.getText()));
                Article a = new Article(0, nom.getText(), desc.getText(),
                        Double.parseDouble(prix.getText()), Double.parseDouble(prixGros.getText()),
                        Integer.parseInt(seuilGros.getText()), Integer.parseInt(stock.getText()), marque);
                adao.ajouter(a);
                JOptionPane.showMessageDialog(this, "Article ajouté !");
            }

        } else if (choix == 1) {
            // Modifier
            List<Article> articles = adao.getAll();
            if (articles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun article disponible.");
                return;
            }

            JComboBox<Article> combo = new JComboBox<>(articles.toArray(new Article[0]));
            combo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel();
                if (value != null) {
                    label.setText(value.getNom() + " (ID " + value.getId() + ")");
                }
                return label;
            });

            int resCombo = JOptionPane.showConfirmDialog(this, combo, "Sélectionnez un article à modifier", JOptionPane.OK_CANCEL_OPTION);
            if (resCombo != JOptionPane.OK_OPTION) return;

            Article a = (Article) combo.getSelectedItem();

            JTextField prix = new JTextField(String.valueOf(a.getPrixUnitaire()));
            JTextField stock = new JTextField(String.valueOf(a.getStock()));
            JTextField prixGros = new JTextField(String.valueOf(a.getPrixGros()));
            JTextField seuilGros = new JTextField(String.valueOf(a.getSeuilGros()));

            Object[] modif = {
                    "Prix unitaire :", prix,
                    "Stock :", stock,
                    "Prix gros :", prixGros,
                    "Seuil gros :", seuilGros
            };

            int res = JOptionPane.showConfirmDialog(this, modif, "Modifier l'article", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                a.setPrixUnitaire(Double.parseDouble(prix.getText()));
                a.setStock(Integer.parseInt(stock.getText()));
                a.setPrixGros(Double.parseDouble(prixGros.getText()));
                a.setSeuilGros(Integer.parseInt(seuilGros.getText()));
                adao.modifier(a);
                JOptionPane.showMessageDialog(this, "Article modifié !");
            }
        } else if (choix == 2) {
            // Supprimer
            List<Article> articles = adao.getAll();
            if (articles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun article disponible.");
                return;
            }

            JComboBox<Article> combo = new JComboBox<>(articles.toArray(new Article[0]));
            combo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel();
                if (value != null) {
                    label.setText(value.getNom() + " (ID " + value.getId() + ")");
                }
                return label;
            });

            int resCombo = JOptionPane.showConfirmDialog(this, combo, "Sélectionnez un article à supprimer", JOptionPane.OK_CANCEL_OPTION);
            if (resCombo != JOptionPane.OK_OPTION) return;

            Article a = (Article) combo.getSelectedItem();
            if (a != null) {
                adao.supprimer(a);
                JOptionPane.showMessageDialog(this, "Article supprimé !");
            }
        }
    }

        private void gererClients () {
            String[] options = {"Afficher", "Ajouter", "Supprimer"};
            int choix = JOptionPane.showOptionDialog(this, "Choisissez une action", "Clients",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (choix == 0) {
                // Afficher
                List<Client> clients = clidao.getAll();
                StringBuilder sb = new StringBuilder("Liste des clients :\n\n");
                for (Client c : clients) {
                    sb.append("ID: ").append(c.getId())
                            .append(" | ").append(c.getNom())
                            .append(" | ").append(c.getEmail()).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Clients", JOptionPane.INFORMATION_MESSAGE);

            } else if (choix == 1) {
                // Ajouter
                JTextField nom = new JTextField();
                JTextField email = new JTextField();
                JTextField mdp = new JTextField();

                Object[] form = {
                        "Nom :", nom,
                        "Email :", email,
                        "Mot de passe :", mdp
                };

                int res = JOptionPane.showConfirmDialog(this, form, "Ajouter un client", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    Client c = new Client(0, nom.getText(), email.getText(), mdp.getText());
                    clidao.ajouter(c);
                    JOptionPane.showMessageDialog(this, "Client ajouté !");
                }

            }else if (choix == 2) {
                    // Supprimer
                    List<Client> clients = clidao.getAll();
                    if (clients.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Aucun client disponible.");
                        return;
                    }

                    JComboBox<Client> combo = new JComboBox<>(clients.toArray(new Client[0]));
                    combo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                        JLabel label = new JLabel();
                        if (value != null) {
                            label.setText(value.getNom() + " (" + value.getEmail() + ")");
                        }
                        return label;
                    });

                    int resCombo = JOptionPane.showConfirmDialog(this, combo, "Sélectionnez un client à supprimer", JOptionPane.OK_CANCEL_OPTION);
                    if (resCombo != JOptionPane.OK_OPTION) return;

                    Client c = (Client) combo.getSelectedItem();
                    if (c != null) {
                        clidao.supprimer(c);
                        JOptionPane.showMessageDialog(this, "Client supprimé !");
                    }
                }
        }

        private void afficherStatistiques () {
            StringBuilder sb = new StringBuilder("📊 Statistiques globales :\n\n");

            var ventes = stats.ventesParArticle();
            sb.append("📦 Ventes par article :\n");
            for (String nom : ventes.keySet()) {
                sb.append("- ").append(nom).append(" : ").append(ventes.get(nom)).append(" vendus\n");
            }

            var notes = stats.noteParArticle();
            sb.append("\n⭐ Notes moyennes :\n");
            for (String nom : notes.keySet()) {
                sb.append("- ").append(nom).append(" : ").append(String.format("%.2f", notes.get(nom))).append(" / 5\n");
            }

            var ca = stats.chiffreParMois();
            sb.append("\n💶 Chiffre par mois :\n");
            for (String mois : ca.keySet()) {
                sb.append("- ").append(mois).append(" : ").append(String.format("%.2f", ca.get(mois))).append(" €\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(450, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Statistiques", JOptionPane.INFORMATION_MESSAGE);
        }
    }

