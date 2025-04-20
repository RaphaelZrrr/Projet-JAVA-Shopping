package Modele;

public class ArticleCommande {
    private Article article;
    private int quantite;

    public ArticleCommande(Article article, int quantite) {
        this.article = article;
        this.quantite = quantite;
    }

    public Article getArticle() {
        return article;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixTotal() {
        if (quantite >= article.getSeuilGros()) {
            int blocs = quantite / article.getSeuilGros();
            int reste = quantite % article.getSeuilGros();
            return blocs * article.getPrixGros() + reste * article.getPrixUnitaire();
        } else {
            return quantite * article.getPrixUnitaire();
        }
    }

    @Override
    public String toString() {
        return "ArticleCommande{" +
                "article=" + article.getNom() +
                ", quantite=" + quantite +
                ", prixTotal=" + getPrixTotal() +
                '}';
    }
}
