package miage.projetindustriel.model;

/**
 * Created by Asus on 18/01/2017.
 */

public class Utilisateur {
    private static String nom;
    private static String prenom;
    private static String pseudo;
    private static Musique currentMusique;

    public Utilisateur(String prenom, String nom, String pseudo) {
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
    }

    public static String getNom() {
        return nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static String getPseudo() {
        return pseudo;
    }

    public static void setNom(String nom) {
        Utilisateur.nom = nom;
    }

    public static void setPrenom(String prenom) {
        Utilisateur.prenom = prenom;
    }

    public static void setPseudo(String pseudo) {
        Utilisateur.pseudo = pseudo;
    }

    public static Musique getCurrentMusique() {
        return currentMusique;
    }

    public static void setCurrentMusique(Musique currentMusique) {
        Utilisateur.currentMusique = currentMusique;
    }
}
