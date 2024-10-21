package me.wky.screenmatch_terminal;

import me.wky.screenmatch_terminal.model.EpisodeData;
import me.wky.screenmatch_terminal.model.SeasonData;
import me.wky.screenmatch_terminal.model.SerieData;
import me.wky.screenmatch_terminal.service.APIConsumer;
import me.wky.screenmatch_terminal.service.DataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchTerminalApplication implements CommandLineRunner {
	public static final String OMDBAPI_KEY= System.getenv("OMDBAPI_KEY");

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchTerminalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var url = "http://www.omdbapi.com/?apikey=" + OMDBAPI_KEY + "&t=Friends";
		var apiConsumer = new APIConsumer();
		var json = apiConsumer.getJsonByUrl(url);
		System.out.println(json);

		var dataConverter = new DataConverter();
		var serieData = dataConverter.convert(json, SerieData.class);
		System.out.println(serieData);

		url = "http://www.omdbapi.com/?apikey=" + OMDBAPI_KEY + "&t=Friends&Season=1&Episode=1";
		json = apiConsumer.getJsonByUrl(url);
		var episodeData = dataConverter.convert(json, EpisodeData.class);
		System.out.println(episodeData);

		List<SeasonData> seasons = new ArrayList<>();

		for (int i = 1; i <= serieData.totalSeasons(); i++) {
			url = "http://www.omdbapi.com/?apikey=" + OMDBAPI_KEY + "&t=Friends&Season=" + i;
			json = apiConsumer.getJsonByUrl(url);
			var seasonData = dataConverter.convert(json, SeasonData.class);
			seasons.add(seasonData);
		}

		for (var season : seasons) {
			System.out.println(season);
		}
	}
}
