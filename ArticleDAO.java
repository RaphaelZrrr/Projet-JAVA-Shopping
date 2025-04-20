package Dao;

import Modele.Article;
import java.util.List;

public interface ArticleDAO {
    List<Article> getAll();
    Article chercher(int id);
    void ajouter(Article article);
    void modifier(Article article);
    void supprimer(Article article);
}
