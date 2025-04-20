package Dao;

import Modele.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {
    private DaoFactory daoFactory;

    public AdminDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Admin> getAll() {
        List<Admin> admins = new ArrayList<>();
        try (Connection conn = daoFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT u.id, u.nom, u.email, u.motDePasse FROM utilisateur u JOIN admin a ON u.id = a.id")) {

            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    @Override
    public Admin chercher(int id) {
        Admin admin = null;
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.id, u.nom, u.email, u.motDePasse FROM utilisateur u JOIN admin a ON u.id = a.id WHERE u.id = ?")) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = new Admin(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("motDePasse")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public void ajouter(Admin admin) {
        try (Connection conn = daoFactory.getConnection()) {
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO utilisateur(nom, email, motDePasse) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt1.setString(1, admin.getNom());
            stmt1.setString(2, admin.getEmail());
            stmt1.setString(3, admin.getMotDePasse());
            stmt1.executeUpdate();

            try (ResultSet rs = stmt1.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    admin.setId(id);

                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO admin(id) VALUES (?)");
                    stmt2.setInt(1, id);
                    stmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Admin admin) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE utilisateur SET nom=?, email=?, motDePasse=? WHERE id=?")) {
            stmt.setString(1, admin.getNom());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getMotDePasse());
            stmt.setInt(4, admin.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Admin admin) {
        try (Connection conn = daoFactory.getConnection()) {
            PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM admin WHERE id=?");
            stmt1.setInt(1, admin.getId());
            stmt1.executeUpdate();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM utilisateur WHERE id=?");
            stmt2.setInt(1, admin.getId());
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
