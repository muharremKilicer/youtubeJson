package com.example.java2.cizgifilmapp2;

public class Bolumler {
    private String title,description,imgUrl,channelTitle,videoId;

    public Bolumler(String title, String description, String imgUrl, String channelTitle, String videoId) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.channelTitle = channelTitle;
        this.videoId = videoId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
