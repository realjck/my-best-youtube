package com.jck.mybestyoutube.pojos;

import java.util.List;

/**
 * POJO du Response API Youtube (objet principal)
 */
public class Response {
    private List<Item> items;
    private String kind;
    private String etag;

    public Response(List<Item> items, String kind, String etag) {
        this.items = items;
        this.kind = kind;
        this.etag = etag;
    }

    public Response() {
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
