package com.example.dough;

import java.util.ArrayList;

public class Series {
    String imgURL;
    String name;
    String siteURL;
    ArrayList<Season> seasons = new ArrayList<>();
    int seasonsNumber;

    public String getImgURL() {
        return imgURL;
    }

    public String getName() {
        return name;
    }

    public int getSeasonsNumber() {
        return seasonsNumber;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setSeasonsNumber(int seasonsNumber) {
        this.seasonsNumber = seasonsNumber;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }

    public Series(String name, String imgURL , String siteURL) {
        this.imgURL = imgURL;
        this.name = name;
        this.siteURL = siteURL;
    }
}
