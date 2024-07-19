package com.jck.mybestyoutube.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class YoutubeVideo {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "titre")
    private String titre;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "url")
    private String url;
    @ColumnInfo(name = "categorie")
    private String categorie;
    @ColumnInfo(name = "favori")
    private Boolean favori;

    public YoutubeVideo(String titre, String description, String url, String categorie, Boolean favori) {
        this.titre = titre;
        this.description = description;
        this.url = url;
        this.categorie = categorie;
        this.favori = favori;
    }
    public YoutubeVideo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Boolean getFavori() {
        return favori;
    }

    public void setFavori(Boolean favori) {
        this.favori = favori;
    }
}
