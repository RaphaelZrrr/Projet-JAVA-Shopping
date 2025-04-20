package Modele;

public class Client extends Utilisateur {

    public Client(int id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
