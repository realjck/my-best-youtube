package com.jck.mybestyoutube.pojos;

/**
 * POJO Snippet de l'API Youtube
 */
public class Snippet {

        private String publishedAt;
        private String channelId;
        private String title;
        private String description;
        private String channelTitle;
        private String categoryId;
        private String liveBroadcastContent;
        private ResourceId resourceId;

    public Snippet(String publishedAt, String channelId, String title, String description, String channelTitle, String categoryId, String liveBroadcastContent, ResourceId resourceId) {
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.title = title;
        this.description = description;
        this.channelTitle = channelTitle;
        this.categoryId = categoryId;
        this.liveBroadcastContent = liveBroadcastContent;
        this.resourceId = resourceId;
    }
    public Snippet(){
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLiveBroadcastContent() {
        return liveBroadcastContent;
    }

    public void setLiveBroadcastContent(String liveBroadcastContent) {
        this.liveBroadcastContent = liveBroadcastContent;
    }

    public ResourceId getResourceId() {
        return resourceId;
    }

    public void setResourceId(ResourceId resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "publishedAt='" + publishedAt + '\'' +
                ", channelId='" + channelId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", liveBroadcastContent='" + liveBroadcastContent + '\'' +
                ", resourceId=" + resourceId +
                '}';
    }
}
