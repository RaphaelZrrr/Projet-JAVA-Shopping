package Dao;

import Modele.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAOImpl implements CommandeDAO {
    private DaoFactory daoFactory;

    public CommandeDAOImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Commande> getAll() {
        List<Commande> commandes = new ArrayList<>();
        ClientDAOImpl clidao = new ClientDAOImpl(daoFactory);
        ArticleCommandeDAOImpl acdao = new ArticleCommandeDAOImpl(daoFactory);

        try (Connection conn = daoFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM commande")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int clientId = rs.getInt("id_client");
                LocalDate date = rs.getDate("dateCommande").toLocalDate();

                Client client = clidao.chercher(clientId);
                if (client == null) {
                    System.err.println("⚠️ Client introuvable pour la commande ID " + id + ". Commande ignorée.");
                    continue;
                }

                List<ArticleCommande> panier = acdao.getByCommandeId(id);
                commandes.add(new Commande(id, client, date, panier));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    @Override
    public Commande chercher(int id) {
        Commande commande = null;
        ClientDAOImpl clidao = new ClientDAOImpl(daoFactory);
        ArticleCommandeDAOImpl acdao = new ArticleCommandeDAOImpl(daoFactory);

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM commande WHERE id = ?")) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int clientId = rs.getInt("id_client");
                    LocalDate date = rs.getDate("dateCommande").toLocalDate();

                    Client client = clidao.chercher(clientId);
                    if (client == null) {
                        System.err.println("⚠️ Client introuvable pour la commande ID " + id);
                        return null;
                    }

                    List<ArticleCommande> panier = acdao.getByCommandeId(id);
                    commande = new Commande(id, client, date, panier);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commande;
    }

    @Override
    public void ajouter(Commande commande) {
        try (Connection conn = daoFactory.getConnection()) {
            // Insertion commande
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO commande(id_client, dateCommande) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setInt(1, commande.getClient().getId());
            stmt.setDate(2, Date.valueOf(commande.getDateCommande()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idCommande = rs.getInt(1);
                    commande.setId(idCommande);

                    // Insertion des articles liés
                    ArticleCommandeDAOImpl acdao = new ArticleCommandeDAOImpl(daoFactory);
                    for (ArticleCommande ac : commande.getPanierArticles()) {
                        acdao.ajouter(idCommande, ac);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
