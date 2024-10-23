package me.wky.screenmatch_terminal.model;

import java.time.LocalDate;

public class Episode {
    private Integer season;
    private String title;
    private Integer episode;
    private Double rating;
    private LocalDate released;

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
            this.released = LocalDate.parse(episodeData.released());
        } catch (Exception e) {
            this.released = null;
        }
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", episode=" + episode +
                ", rating='" + rating + '\'' +
                ", released='" + released;
    }

    public Double getRating() {
        return rating;
    }
}