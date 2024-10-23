package me.wky.screenmatch_terminal.model;

import java.time.LocalDate;

public class Episode {
    private Integer season;
    private String title;
    private Integer episode;
    private Double rating;

    private LocalDate releaseDate;

    public Episode(Integer season, EpisodeData episodeData) {
        this.season = season;
        this.title = episodeData.title();
        this.episode = episodeData.episode();

        try {
            this.rating = Double.valueOf(episodeData.rating());
        } catch (NumberFormatException e) {
            this.rating = 0.0;
        }

        try {
            this.releaseDate = LocalDate.parse(episodeData.released());
        } catch (Exception e) {
            this.releaseDate = null;
        }
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", episode=" + episode +
                ", rating='" + rating + '\'' +
                ", released='" + releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public Integer getSeason() {
        return season;
    }
}