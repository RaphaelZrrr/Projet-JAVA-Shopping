package Dao;

import Modele.Marque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarqueDAOImpl implements MarqueDAO {
    private DaoFactory daoFactory;

    public MarqueDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Marque> getAll() {
        List<Marque> marques = new ArrayList<>();
        try (Connection conn = daoFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM marque")) {

            while (rs.next()) {
                marques.add(new Marque(rs.getInt("id"), rs.getString("nom")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marques;
    }

    @Override
    public Marque chercher(int id) {
        Marque marque = null;
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM marque WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    marque = new Marque(rs.getInt("id"), rs.getString("nom"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marque;
    }

    @Override
    public void ajouter(Marque marque) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO marque(nom) VALUES(?)")) {
            stmt.setString(1, marque.getNom());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Marque marque) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE marque SET nom=? WHERE id=?")) {
            stmt.setString(1, marque.getNom());
            stmt.setInt(2, marque.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Marque marque) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM marque WHERE id=?")) {
            stmt.setInt(1, marque.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
