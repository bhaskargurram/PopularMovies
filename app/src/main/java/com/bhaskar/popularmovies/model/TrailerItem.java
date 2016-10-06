package com.bhaskar.popularmovies.model;

/**
 * Created by bhaskar on 4/2/16.
 * Used for Object oriented modelling of trailers
 */
public class TrailerItem {
String name,key;

    public TrailerItem(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
