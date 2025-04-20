package Dao;

import Modele.Marque;
import java.util.List;

public interface MarqueDAO {
    List<Marque> getAll();
    Marque chercher(int id);
    void ajouter(Marque marque);
    void modifier(Marque marque);
    void supprimer(Marque marque);
}
