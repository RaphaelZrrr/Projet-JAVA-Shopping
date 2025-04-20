package Dao;

import Modele.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl implements ClientDAO {
    private DaoFactory daoFactory;

    public ClientDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        try (Connection conn = daoFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT u.id, u.nom, u.email, u.motDePasse FROM utilisateur u JOIN client c ON u.id = c.id")) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client chercher(int id) {
        Client client = null;
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.id, u.nom, u.email, u.motDePasse FROM utilisateur u JOIN client c ON u.id = c.id WHERE u.id = ?")) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    client = new Client(
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
        return client;
    }

    @Override
    public void ajouter(Client client) {
        try (Connection conn = daoFactory.getConnection()) {
            // Insertion dans utilisateur
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO utilisateur(nom, email, motDePasse) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt1.setString(1, client.getNom());
            stmt1.setString(2, client.getEmail());
            stmt1.setString(3, client.getMotDePasse());
            stmt1.executeUpdate();

            try (ResultSet rs = stmt1.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    client.setId(id);

                    // Insertion dans client
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO client(id) VALUES (?)");
                    stmt2.setInt(1, id);
                    stmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Client client) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE utilisateur SET nom=?, email=?, motDePasse=? WHERE id=?")) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getMotDePasse());
            stmt.setInt(4, client.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Client client) {
        try (Connection conn = daoFactory.getConnection()) {
            PreparedStatement stmtClient = conn.prepareStatement("DELETE FROM client WHERE id=?");
            stmtClient.setInt(1, client.getId());
            stmtClient.executeUpdate();

            PreparedStatement stmtUser = conn.prepareStatement("DELETE FROM utilisateur WHERE id=?");
            stmtUser.setInt(1, client.getId());
            stmtUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
