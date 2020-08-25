package com.example.dough;

import java.io.File;

public class Movie {
     private String name;
     private String imageurl;
     private String vidurl;
     private File movieFile;

    public String getVidurl() {
        return vidurl;
    }

    public void setVidUrl(String vidurl) {
        this.vidurl = vidurl;
    }

    public File getMovieFile() {
        return movieFile;
    }

    public Movie(String name, String imageurl, String vidurl) {
        this.name = name;
        this.imageurl = imageurl;
        this.vidurl = vidurl;
      }

    public void setMovieFile(File movieFile) {
        this.movieFile = movieFile;
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
