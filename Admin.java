package Modele;

public class Admin extends Utilisateur {

    public Admin(int id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
