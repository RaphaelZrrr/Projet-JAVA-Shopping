package Dao;

import Modele.Client;
import java.util.List;

public interface ClientDAO {
    List<Client> getAll();
    Client chercher(int id);
    void ajouter(Client client);
    void modifier(Client client);
    void supprimer(Client client);
}
