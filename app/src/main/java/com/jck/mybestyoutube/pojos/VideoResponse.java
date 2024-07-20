package com.jck.mybestyoutube.pojos;

import java.util.List;

public class VideoResponse {
    private List<Item> items;
    private String kind;
    private String etag;

    public VideoResponse(List<Item> items, String kind, String etag) {
        this.items = items;
        this.kind = kind;
        this.etag = etag;
    }

    public VideoResponse() {
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }
}
