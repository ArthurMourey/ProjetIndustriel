package miage.projetindustriel.model;

import java.util.List;

public class Album {

    private int id;
    private String nom;
    private String coverImage;
    private String dateSortie;
    private int nbSons;
    private List<Musique> musiques;

    public Album(int id, String nom, String coverImage, String dateSortie, int nbSons) {
        this.id = id;
        this.nom = nom;
        this.coverImage = coverImage;
        this.dateSortie = dateSortie;
        this.nbSons = nbSons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
    }

    public int getNbSons() {
        return nbSons;
    }

    public void setNbSons(int nbSons) {
        this.nbSons = nbSons;
    }

    public List<Musique> getMusiques() {
        return musiques;
    }

    public void setMusiques(List<Musique> musiques) {
        this.musiques = musiques;
    }
}
