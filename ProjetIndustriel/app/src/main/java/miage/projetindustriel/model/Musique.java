package miage.projetindustriel.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by utilisateur on 08/01/2017.
 */

public class Musique implements Parcelable {
    private int id;
    private int idAlbum;
    private String titre;
    private String duree;
    private String coverPhoto;
    private String urlSon;

    public Musique(int id, int idAlbum, String titre, String duree, String coverPhoto, String urlSon) {
        this.id = id;
        this.idAlbum = idAlbum;
        this.titre = titre;
        this.duree = duree;
        this.coverPhoto = coverPhoto;
        this.urlSon = urlSon;
    }

    public Musique(Parcel in){
        this.id = in.readInt();
        this.idAlbum = in.readInt();
        this.titre = in.readString();
        this.duree = in.readString();
        this.coverPhoto = in.readString();
        this.urlSon = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getUrlSon() {
        return urlSon;
    }

    public void setUrlSon(String urlSon) {
        this.urlSon = urlSon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idAlbum);
        dest.writeString(titre);
        dest.writeString(duree);
        dest.writeString(coverPhoto);
        dest.writeString(urlSon);
    }

    public static final Parcelable.Creator<Musique> CREATOR = new Creator() {
        @Override
        public Musique createFromParcel(Parcel source) {
            return new Musique(source);
        }

        @Override
        public Musique[] newArray(int size) {
            return new Musique[size];
        }
    };
}
