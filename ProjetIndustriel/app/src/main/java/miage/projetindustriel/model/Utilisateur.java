package miage.projetindustriel.model;

/**
 * Created by Asus on 18/01/2017.
 */

public class Utilisateur {
    private String nom;
    private String prenom;
    private String pseudo;
    private Musique musiqueActuelle;

    public Utilisateur(String prenom, String nom, String pseudo) {
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public Musique getMusiqueActuelle() {
        return musiqueActuelle;
    }

    public void setMusiqueActuelle(Musique musiqueActuelle) {
        this.musiqueActuelle = musiqueActuelle;
    }
}
