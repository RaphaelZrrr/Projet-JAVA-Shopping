package Dao;

import Modele.NoteArticle;
import java.util.List;

public interface NoteArticleDAO {
    void ajouter(NoteArticle note);
    NoteArticle chercher(int clientId, int articleId);
    List<NoteArticle> getAll();
    double moyennePourArticle(int articleId);
}
