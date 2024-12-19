package me.wky.screenmatch_terminal.repository;

import me.wky.screenmatch_terminal.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {
}
