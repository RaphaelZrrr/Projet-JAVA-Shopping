package Dao;

import Modele.Admin;
import java.util.List;

public interface AdminDAO {
    List<Admin> getAll();
    Admin chercher(int id);
    void ajouter(Admin admin);
    void modifier(Admin admin);
    void supprimer(Admin admin);
}
