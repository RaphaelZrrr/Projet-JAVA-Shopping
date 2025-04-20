package Dao;

import Modele.Article;
import Modele.Marque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAOImpl implements ArticleDAO {
    private DaoFactory daoFactory;

    public ArticleDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    private Marque getMarqueById(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM marque WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Marque(rs.getInt("id"), rs.getString("nom"));
                }
            }
        }
        return null;
    }

    @Override
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        try (Connection conn = daoFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM article")) {

            while (rs.next()) {
                Marque m = getMarqueById(rs.getInt("id_marque"), conn);
                Article a = new Article(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prixUnitaire"),
                        rs.getDouble("prixGros"),
                        rs.getInt("seuilGros"),
                        rs.getInt("stock"),
                        m
                );
                articles.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public Article chercher(int id) {
        Article article = null;
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM article WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Marque m = getMarqueById(rs.getInt("id_marque"), conn);
                    article = new Article(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getDouble("prixUnitaire"),
                            rs.getDouble("prixGros"),
                            rs.getInt("seuilGros"),
                            rs.getInt("stock"),
                            m
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public void ajouter(Article a) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO article(nom, description, prixUnitaire, prixGros, seuilGros, stock, id_marque) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, a.getNom());
            stmt.setString(2, a.getDescription());
            stmt.setDouble(3, a.getPrixUnitaire());
            stmt.setDouble(4, a.getPrixGros());
            stmt.setInt(5, a.getSeuilGros());
            stmt.setInt(6, a.getStock());
            stmt.setInt(7, a.getMarque().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Article a) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE article SET nom=?, description=?, prixUnitaire=?, prixGros=?, seuilGros=?, stock=?, id_marque=? WHERE id=?")) {
            stmt.setString(1, a.getNom());
            stmt.setString(2, a.getDescription());
            stmt.setDouble(3, a.getPrixUnitaire());
            stmt.setDouble(4, a.getPrixGros());
            stmt.setInt(5, a.getSeuilGros());
            stmt.setInt(6, a.getStock());
            stmt.setInt(7, a.getMarque().getId());
            stmt.setInt(8, a.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Article a) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM article WHERE id=?")) {
            stmt.setInt(1, a.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
