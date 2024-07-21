package com.jck.mybestyoutube.pojos;

/**
 * POJO Item de l'API Youtube
 */
public class Item {

    private String kind;
    private String etag;
    private String id;
    private Snippet snippet;

    public Item(String kind, String etag, String id, Snippet snippet) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.snippet = snippet;
    }

    public Item() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "Item{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", id='" + id + '\'' +
                ", snippet=" + snippet +
                '}';
    }
}
