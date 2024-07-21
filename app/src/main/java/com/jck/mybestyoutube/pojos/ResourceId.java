package com.jck.mybestyoutube.pojos;

/**
 * POJO ResourceId de l'API Youtube
 */
public class ResourceId {
    private String kind;
    private String videoId;

    public ResourceId(String kind, String videoId) {
        this.kind = kind;
        this.videoId = videoId;
    }

    public ResourceId() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return "ResourceId{" +
                "kind='" + kind + '\'' +
                ", videoId='" + videoId + '\'' +
                '}';
    }
}
