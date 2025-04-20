package Modele;

public class NoteArticle {
    private Client client;
    private Article article;
    private int note; // valeur de 1 à 5

    public NoteArticle(Client client, Article article, int note) {
        this.client = client;
        this.article = article;
        this.note = note;
    }

    public Client getClient() {
        return client;
    }

    public Article getArticle() {
        return article;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "NoteArticle{" +
                "client=" + client.getNom() +
                ", article=" + article.getNom() +
                ", note=" + note +
                '}';
    }
}
