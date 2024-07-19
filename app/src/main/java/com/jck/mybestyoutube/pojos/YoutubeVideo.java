package com.jck.mybestyoutube.pojos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class YoutubeVideo {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "youtube_id")
    private String youtube_id;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "favorite")
    private Boolean favorite;

    public YoutubeVideo(String title, String description, String youtube_id, String category, Boolean favorite) {
        this.title = title;
        this.description = description;
        this.youtube_id = youtube_id;
        this.category = category;
        this.favorite = favorite;
    }
    public YoutubeVideo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
