package Modele;

import java.time.LocalDate;
import java.util.List;

public class Commande {
    private int id;
    private Client client;
    private LocalDate dateCommande;
    private List<ArticleCommande> panierArticles;

    public Commande(int id, Client client, LocalDate dateCommande, List<ArticleCommande> panierArticles) {
        this.id = id;
        this.client = client;
        this.dateCommande = dateCommande;
        this.panierArticles = panierArticles;
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public List<ArticleCommande> getPanierArticles() {
        return panierArticles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public void setPanierArticles(List<ArticleCommande> panierArticles) {
        this.panierArticles = panierArticles;
    }

    public double calculerTotal() {
        return panierArticles.stream()
                .mapToDouble(ArticleCommande::getPrixTotal)
                .sum();
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", client=" + client.getNom() +
                ", dateCommande=" + dateCommande +
                ", total=" + calculerTotal() +
                '}';
    }
}
