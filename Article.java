package Modele;

public class Article {
    private int id;
    private String nom;
    private String description;
    private double prixUnitaire;
    private double prixGros;
    private int seuilGros;
    private int stock;
    private Marque marque;

    public Article(int id, String nom, String description, double prixUnitaire, double prixGros, int seuilGros, int stock, Marque marque) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.prixGros = prixGros;
        this.seuilGros = seuilGros;
        this.stock = stock;
        this.marque = marque;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public double getPrixGros() {
        return prixGros;
    }

    public int getSeuilGros() {
        return seuilGros;
    }

    public int getStock() {
        return stock;
    }

    public Marque getMarque() {
        return marque;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public void setPrixGros(double prixGros) {
        this.prixGros = prixGros;
    }

    public void setSeuilGros(int seuilGros) {
        this.seuilGros = seuilGros;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                ", prixGros=" + prixGros +
                ", seuilGros=" + seuilGros +
                ", stock=" + stock +
                ", marque=" + (marque != null ? marque.getNom() : "null") +
                '}';
    }
}
