package com.ysharma.animetracker.model;

public class AnimeItem {
    public String title;
    public String imageUrl;
    public int episodes;
    public double score;
    public String year;
    public int watchedEpisodes;
    public String synopsis;
    public String firebaseKey;

    public AnimeItem() {}

    public AnimeItem(String title, String imageUrl, int episodes, double score, String year, int watchedEpisodes, String synopsis) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.episodes = episodes;
        this.score = score;
        this.year = year;
        this.watchedEpisodes = watchedEpisodes;
        this.synopsis = synopsis;
    }
}

