package com.example.dough;

import java.util.ArrayList;

public class Season implements Comparable {
    String seriesName;
    ArrayList<Episode> episodes;
    int seasonNumber;

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public Season(String seriesName, ArrayList<Episode> episodes , int seasonNumber) {
        this.seriesName = seriesName;
        this.episodes = episodes;
        this.seasonNumber = seasonNumber;
    }

    public String getSeriesName() {
        return seriesName;
    }

    @Override
    public int compareTo(Object o) {
        return  seasonNumber - ((Season)o).getSeasonNumber();
    }
}
