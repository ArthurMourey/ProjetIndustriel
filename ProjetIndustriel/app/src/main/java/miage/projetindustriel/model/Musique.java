package miage.projetindustriel.model;

/**
 * Created by Asus on 18/01/2017.
 */
public class Musique {
    private String titre;
    private String artiste;

    public Musique(String titre, String artiste) {
        this.titre = titre;
        this.artiste = artiste;
    }

    public String getTitre() {
        return titre;
    }

    public String getArtiste() {
        return artiste;
    }
}
