package com.example.dough;

import java.io.File;
import java.util.ArrayList;

public class Movie {
    String vidURL;
    String imgURL;
    String name;
    File movieFile;

    public void setName(String name) {
        this.name = name;
    }

    public void setMovieFile(File movieFile) {
        this.movieFile = movieFile;
    }

    public File getMovieFile() {
        return movieFile;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getVidURL() {
        return vidURL;
    }

    public String getName() {
        return name;
    }

    String siteUrl;

    public ArrayList<String> getUrls() {
        return urls;
    }

    public Movie(String name, String imgURL, String vidURL) {
        this.vidURL = vidURL;
        this.imgURL = imgURL;
        this.name = name;
        urls = new ArrayList<>();
        urls.add(vidURL);
    }

    ArrayList<String> urls;
}
