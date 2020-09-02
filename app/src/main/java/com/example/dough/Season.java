package com.example.dough;

import java.util.ArrayList;

public class Season {
    String seriesName;
    ArrayList<Episode> episodes;

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public Season(String seriesName, ArrayList<Episode> episodes) {
        this.seriesName = seriesName;
        this.episodes = episodes;
    }
}
