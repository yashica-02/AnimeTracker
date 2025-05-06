package com.ysharma.animetracker.model;

public class Anime {
    private int mal_id;
    private String title;
    private int episodes;
    private double score;
    private Integer year;
    private Images images;
    public String synopsis;

    public String getTitle() {
        return title;
    }

    public int getEpisodes() {
        return episodes;
    }

    public double getScore() {
        return score;
    }

    public Integer getYear() {
        return year;
    }

    public String getImageUrl() {
        return images.jpg.image_url;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public static class Images {
        public Jpg jpg;
    }

    public static class Jpg {
        public String image_url;
    }
}
