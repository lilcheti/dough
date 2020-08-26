package com.example.dough;

public class Series extends Movie{
    private String directoryName;
    private String name;
    private String parentDirectory;

    public Series(String name, String imageurl, String vidurl) {
        super(name, imageurl, vidurl);
    }
}
