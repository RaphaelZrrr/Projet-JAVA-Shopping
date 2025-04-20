package Dao;

import Modele.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteArticleDAOImpl implements NoteArticleDAO {
    private DaoFactory daoFactory;

    public NoteArticleDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void ajouter(NoteArticle note) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO notearticle(id_client, id_article, note) VALUES (?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE note = ?")) {
            stmt.setInt(1, note.getClient().getId());
            stmt.setInt(2, note.getArticle().getId());
            stmt.setInt(3, note.getNote());
            stmt.setInt(4, note.getNote());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public NoteArticle chercher(int clientId, int articleId) {
        NoteArticle note = null;
        ClientDAOImpl clidao = new ClientDAOImpl(daoFactory);
        ArticleDAOImpl articledao = new ArticleDAOImpl(daoFactory);

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT note FROM notearticle WHERE id_client = ? AND id_article = ?")) {

            stmt.setInt(1, clientId);
            stmt.setInt(2, articleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = clidao.chercher(clientId);
                    Article article = articledao.chercher(articleId);
                    note = new NoteArticle(client, article, rs.getInt("note"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return note;
    }

    @Override
    public List<NoteArticle> getAll() {
        List<NoteArticle> notes = new ArrayList<>();
        ClientDAOImpl clidao = new ClientDAOImpl(daoFactory);
        ArticleDAOImpl articledao = new ArticleDAOImpl(daoFactory);

        try (Connection conn = daoFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM notearticle")) {

            while (rs.next()) {
                Client client = clidao.chercher(rs.getInt("id_client"));
                Article article = articledao.chercher(rs.getInt("id_article"));
                int note = rs.getInt("note");
                notes.add(new NoteArticle(client, article, note));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    @Override
    public double moyennePourArticle(int articleId) {
        double moyenne = 0;
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT AVG(note) AS moyenne FROM notearticle WHERE id_article = ?")) {
            stmt.setInt(1, articleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    moyenne = rs.getDouble("moyenne");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moyenne;
    }
}
