package com.example.dough;

public class movie {
     private String name;
     private String imageurl;
     private String vidurl;

    public String getVidurl() {
        return vidurl;
    }

    public void setVidUrl(String vidurl) {
        this.vidurl = vidurl;
    }

    public movie(String name, String imageurl, String vidurl) {
        this.name = name;
        this.imageurl = imageurl;
        this.vidurl = vidurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public void setImageUrl(String imageurl) {
        this.imageurl = imageurl;
    }
}
