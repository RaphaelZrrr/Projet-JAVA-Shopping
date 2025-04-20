package Vue;

import Dao.*;
import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class VueClient extends JFrame {

    private DaoFactory dao;
    private Client client;
    private ArticleDAOImpl adao;
    private CommandeDAOImpl cdao;
    private NoteArticleDAOImpl notedao;

    public VueClient(DaoFactory daoFactory, Client client) {
        this.dao = daoFactory;
        this.client = client;

        this.adao = new ArticleDAOImpl(dao);
        this.cdao = new CommandeDAOImpl(dao);
        this.notedao = new NoteArticleDAOImpl(dao);

        setTitle("Espace Client");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel labelId = new JLabel("ID Client : " + client.getId());
        labelId.setFont(new Font("Arial", Font.BOLD, 14));
        labelId.setForeground(Color.BLUE);
        haut.add(labelId);

        JButton btnVoirCatalogue = new JButton("Voir Catalogue");
        JButton btnCommander = new JButton("Passer Commande");
        JButton btnNoter = new JButton("Noter un Article");
        JButton btnHistorique = new JButton("Historique");
        JButton btnRetour = new JButton("⬅️ Retour");

        JPanel actions = new JPanel(new GridLayout(5, 1, 10, 10));
        actions.add(btnVoirCatalogue);
        actions.add(btnCommander);
        actions.add(btnNoter);
        actions.add(btnHistorique);
        actions.add(btnRetour);

        panel.add(haut, BorderLayout.NORTH);
        panel.add(actions, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(panel);

        // === Boutons ===
        btnVoirCatalogue.addActionListener(e -> afficherCatalogue());
        btnCommander.addActionListener(e -> passerCommande());
        btnNoter.addActionListener(e -> noterArticle());
        btnHistorique.addActionListener(e -> afficherHistorique());
        btnRetour.addActionListener(e -> {
            new FenetrePrincipale();
            dispose();
        });

        setVisible(true);
    }

    private void afficherCatalogue() {
        List<Marque> marques = new MarqueDAOImpl(dao).getAll();
        marques.add(0, new Marque(0, "Toutes les marques"));

        JComboBox<Marque> combo = new JComboBox<>(marques.toArray(new Marque[0]));
        combo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value != null) {
                label.setText(value.getNom());
            }
            return label;
        });

        int choix = JOptionPane.showConfirmDialog(this, combo, "Filtrer par marque", JOptionPane.OK_CANCEL_OPTION);

        if (choix != JOptionPane.OK_OPTION) return;

        Marque marqueChoisie = (Marque) combo.getSelectedItem();
        List<Article> articles = adao.getAll();

        StringBuilder sb = new StringBuilder("📦 Catalogue des articles :\n\n");

        for (Article a : articles) {
            boolean afficher = (marqueChoisie == null || marqueChoisie.getId() == 0 || a.getMarque().getId() == marqueChoisie.getId());
            if (afficher) {
                sb.append("- ").append(a.getNom())
                        .append(" | ").append(a.getPrixUnitaire()).append(" €")
                        .append(" | Stock : ").append(a.getStock());

                if (a.getSeuilGros() > 0 && a.getPrixGros() > 0) {
                    sb.append(" | Promo : ").append(a.getSeuilGros()).append(" pour ").append(a.getPrixGros()).append(" €");
                }

                sb.append(" | Marque : ").append(a.getMarque().getNom()).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Catalogue", JOptionPane.INFORMATION_MESSAGE);
    }

    private void passerCommande() {
        List<Article> articles = adao.getAll();
        List<ArticleCommande> panier = new ArrayList<>();

        boolean continuer = true;

        while (continuer) {
            // Création du menu déroulant
            JComboBox<Article> comboArticles = new JComboBox<>(articles.toArray(new Article[0]));
            comboArticles.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel();
                if (value != null) {
                    String text = value.getNom() + " - " + value.getPrixUnitaire() + "€ - Stock : " + value.getStock();
                    if (value.getSeuilGros() > 0 && value.getPrixGros() > 0) {
                        text += " | Promo : " + value.getSeuilGros() + " pour " + value.getPrixGros() + "€";
                    }
                    label.setText(text);
                }
                return label;
            });


            int selection = JOptionPane.showConfirmDialog(this, comboArticles, "Choisir un article", JOptionPane.OK_CANCEL_OPTION);

            if (selection != JOptionPane.OK_OPTION) break;

            Article articleChoisi = (Article) comboArticles.getSelectedItem();

            if (articleChoisi == null) {
                JOptionPane.showMessageDialog(this, "Aucun article sélectionné.");
                continue;
            }

            String qteStr = JOptionPane.showInputDialog(this, "Quantité pour " + articleChoisi.getNom() + " (stock : " + articleChoisi.getStock() + ")");
            if (qteStr == null) break;

            int qte;
            try {
                qte = Integer.parseInt(qteStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantité invalide.");
                continue;
            }

            if (qte <= 0 || qte > articleChoisi.getStock()) {
                JOptionPane.showMessageDialog(this, "Quantité non disponible en stock.");
                continue;
            }

            panier.add(new ArticleCommande(articleChoisi, qte));

            int choix = JOptionPane.showConfirmDialog(this, "Ajouter un autre article ?", "Continuer ?", JOptionPane.YES_NO_OPTION);
            if (choix != JOptionPane.YES_OPTION) {
                continuer = false;
            }
        }

        if (!panier.isEmpty()) {
            Commande commande = new Commande(0, client, LocalDate.now(), panier);
            cdao.ajouter(commande);

            // Mise à jour des stocks
            for (ArticleCommande ac : panier) {
                Article a = ac.getArticle();
                int nouveauStock = a.getStock() - ac.getQuantite();
                a.setStock(nouveauStock);
                adao.modifier(a);
            }

            JOptionPane.showMessageDialog(this, "✅ Commande enregistrée !\nTotal : " + commande.calculerTotal() + " €");
        } else {
            JOptionPane.showMessageDialog(this, "Commande annulée.");
        }
    }

    private void noterArticle() {
        List<Commande> commandesClient = cdao.getAll().stream()
                .filter(c -> c.getClient().getId() == client.getId())
                .toList();

        List<Article> articlesCommandes = new ArrayList<>();

        for (Commande cmd : commandesClient) {
            for (ArticleCommande ac : cmd.getPanierArticles()) {
                Article article = ac.getArticle();
                // Ajoute seulement si pas déjà présent (évite les doublons)
                if (articlesCommandes.stream().noneMatch(a -> a.getId() == article.getId())) {
                    articlesCommandes.add(article);
                }
            }
        }

        if (articlesCommandes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun article à noter. Vous devez d'abord passer une commande.");
            return;
        }

        // Crée le menu déroulant avec seulement les noms visibles
        JComboBox<Article> combo = new JComboBox<>(articlesCommandes.toArray(new Article[0]));
        combo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value != null) {
                label.setText(value.getNom()); // Affiche uniquement le nom de l'article
            }
            return label;
        });

        int choixArticle = JOptionPane.showConfirmDialog(this, combo, "Choisissez un article à noter", JOptionPane.OK_CANCEL_OPTION);
        if (choixArticle != JOptionPane.OK_OPTION) return;

        Article article = (Article) combo.getSelectedItem();
        if (article == null) return;

        NoteArticle dejaNote = notedao.chercher(client.getId(), article.getId());
        if (dejaNote != null) {
            JOptionPane.showMessageDialog(this, "⚠️ Vous avez déjà noté cet article : " + dejaNote.getNote() + "/5");
        }

        String noteStr = JOptionPane.showInputDialog(this, "Entrez votre note (1 à 5) pour " + article.getNom() + ":");

        int note;
        try {
            note = Integer.parseInt(noteStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Note invalide.");
            return;
        }

        if (note < 1 || note > 5) {
            JOptionPane.showMessageDialog(this, "Note hors de l'intervalle (1 à 5).");
            return;
        }

        NoteArticle noteArticle = new NoteArticle(client, article, note);
        notedao.ajouter(noteArticle);

        JOptionPane.showMessageDialog(this, "✅ Note enregistrée !");
    }

    private void afficherHistorique() {
        List<Commande> commandesClient = cdao.getAll().stream()
                .filter(c -> c.getClient().getId() == client.getId())
                .toList();

        if (commandesClient.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucune commande enregistrée.");
            return;
        }

        StringBuilder sb = new StringBuilder(" Historique des commandes :\n\n");

        for (Commande cmd : commandesClient) {
            sb.append("Commande #").append(cmd.getId())
                    .append(" | Date : ").append(cmd.getDateCommande()).append("\n");

            for (ArticleCommande ac : cmd.getPanierArticles()) {
                sb.append(" - ")
                        .append(ac.getArticle().getNom())
                        .append(" x").append(ac.getQuantite());

                if (ac.getArticle().getSeuilGros() > 0 && ac.getArticle().getPrixGros() > 0) {
                    sb.append(" (Promo : ").append(ac.getArticle().getSeuilGros()).append(" pour ").append(ac.getArticle().getPrixGros()).append("€)");
                }

                NoteArticle note = notedao.chercher(client.getId(), ac.getArticle().getId());
                if (note != null) {
                    sb.append("  Note: ").append(note.getNote()).append("/5");
                }


                sb.append("\n");
            }

            sb.append("💶 Total : ").append(cmd.calculerTotal()).append(" €\n\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(this, scroll, "Historique des commandes", JOptionPane.INFORMATION_MESSAGE);
    }

}
