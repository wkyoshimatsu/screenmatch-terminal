package me.wky.screenmatch_terminal.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record SerieData(@JsonAlias("Title") String title,
                        Integer totalSeasons,
                        @JsonAlias("imdbRating")String rating) {
}
