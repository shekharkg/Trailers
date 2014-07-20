package com.trailer.dao;

/**
 * Created by SKG on 20-Jul-14.
 */
public class TrailerDao {
    private String title;
    private String description;
    private String thumbnailUrl;
    private String url;

    public TrailerDao(String title, String description, String thumbnailUrl, String url) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

}
