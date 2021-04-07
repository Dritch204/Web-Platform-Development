package com.example.ukearthquakes;

//Drew Ritchie S1710460
public class EarthQuakeModels {

    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String earthQuakeLat;
    private String earthQuakeLong;


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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getEarthQuakeLat() {
        return earthQuakeLat;
    }

    public void setEarthQuakeLat(String earthQuakeLat) {
        this.earthQuakeLat = earthQuakeLat;
    }

    public String getEarthQuakeLong() {
        return earthQuakeLong;
    }

    public void setEarthQuakeLong(String earthQuakeLong) {
        this.earthQuakeLong = earthQuakeLong;
    }
}
