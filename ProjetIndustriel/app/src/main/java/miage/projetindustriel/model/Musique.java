package miage.projetindustriel.model;

/**
 * Created by Asus on 18/01/2017.
 */
public class Musique {
    private static String titre;
    private static String artiste;

    public Musique(String titre, String artiste) {
        this.titre = titre;
        this.artiste = artiste;
    }

    public static String getMusiqueActuelle() {
        return titre+" - "+artiste;
    }

    public static String getTitre() {
        return titre;
    }

    public static String getArtiste() {
        return artiste;
    }
}
