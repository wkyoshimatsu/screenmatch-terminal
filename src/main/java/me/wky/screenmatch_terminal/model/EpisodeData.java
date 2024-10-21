package me.wky.screenmatch_terminal.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(@JsonAlias("Title") String title,
                          @JsonAlias("Episode") Integer episodeNumber,
                          @JsonAlias("imdbRating") String rating,
                          @JsonAlias("Released") String released) {
}
