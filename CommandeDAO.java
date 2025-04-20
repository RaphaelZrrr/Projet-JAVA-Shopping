package Dao;

import Modele.Commande;
import java.util.List;

public interface CommandeDAO {
    List<Commande> getAll();
    Commande chercher(int id);
    void ajouter(Commande commande);
}
