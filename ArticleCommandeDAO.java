package Dao;

import Modele.ArticleCommande;
import java.util.List;

public interface ArticleCommandeDAO {
    List<ArticleCommande> getByCommandeId(int commandeId);
    void ajouter(int commandeId, ArticleCommande articleCommande);
}
