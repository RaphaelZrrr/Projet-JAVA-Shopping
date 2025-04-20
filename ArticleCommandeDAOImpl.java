package Dao;

import Modele.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleCommandeDAOImpl implements ArticleCommandeDAO {
    private DaoFactory daoFactory;

    public ArticleCommandeDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<ArticleCommande> getByCommandeId(int commandeId) {
        List<ArticleCommande> liste = new ArrayList<>();
        ArticleDAOImpl articledao = new ArticleDAOImpl(daoFactory);

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM articlecommande WHERE id_commande = ?")) {

            stmt.setInt(1, commandeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Article a = articledao.chercher(rs.getInt("id_article"));
                    int qte = rs.getInt("quantite");
                    liste.add(new ArticleCommande(a, qte));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    @Override
    public void ajouter(int commandeId, ArticleCommande ac) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO articlecommande(id_commande, id_article, quantite) VALUES (?, ?, ?)")) {
            stmt.setInt(1, commandeId);
            stmt.setInt(2, ac.getArticle().getId());
            stmt.setInt(3, ac.getQuantite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
