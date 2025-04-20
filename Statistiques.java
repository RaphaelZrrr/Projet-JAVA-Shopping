package Modele;

import Dao.DaoFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Statistiques {

    private DaoFactory daoFactory;

    public Statistiques(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public Map<String, Integer> ventesParArticle() {
        Map<String, Integer> resultats = new HashMap<>();

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT a.nom, SUM(ac.quantite) AS total " +
                             "FROM articlecommande ac " +
                             "JOIN article a ON ac.id_article = a.id " +
                             "GROUP BY a.nom " +
                             "ORDER BY total DESC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultats.put(rs.getString("nom"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultats;
    }

    public Map<String, Double> noteParArticle() {
        Map<String, Double> notes = new HashMap<>();

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT a.nom, AVG(n.note) AS moyenne " +
                             "FROM notearticle n " +
                             "JOIN article a ON n.id_article = a.id " +
                             "GROUP BY a.nom " +
                             "ORDER BY moyenne DESC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notes.put(rs.getString("nom"), rs.getDouble("moyenne"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public Map<String, Double> chiffreParMois() {
        Map<String, Double> resultats = new HashMap<>();

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT DATE_FORMAT(c.dateCommande, '%Y-%m') AS mois, " +
                             "SUM(CASE " +
                             "WHEN ac.quantite >= a.seuilGros THEN " +
                             "    (FLOOR(ac.quantite / a.seuilGros) * a.prixGros + " +
                             "     (ac.quantite % a.seuilGros) * a.prixUnitaire) " +
                             "ELSE ac.quantite * a.prixUnitaire END) AS total " +
                             "FROM commande c " +
                             "JOIN articlecommande ac ON c.id = ac.id_commande " +
                             "JOIN article a ON ac.id_article = a.id " +
                             "GROUP BY mois " +
                             "ORDER BY mois DESC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultats.put(rs.getString("mois"), rs.getDouble("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultats;
    }
}
