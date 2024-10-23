package me.wky.screenmatch_terminal.terminal;

import me.wky.screenmatch_terminal.model.EpisodeData;
import me.wky.screenmatch_terminal.model.SeasonData;
import me.wky.screenmatch_terminal.model.SerieData;
import me.wky.screenmatch_terminal.service.APIConsumer;
import me.wky.screenmatch_terminal.service.DataConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Terminal {
    private final String OMDBAPI_KEY= System.getenv("OMDBAPI_KEY");

    private final String API_URL = "http://www.omdbapi.com/?apikey=" + OMDBAPI_KEY + "&t=";

    private Scanner scanner = new Scanner(System.in);

    private APIConsumer apiConsumer = new APIConsumer();

    private DataConverter dataConverter = new DataConverter();

    public void printMenu() {
        System.out.println("Digite o nome da série desejada:");
        var serieName = scanner.nextLine().toLowerCase().replace(" ", "+");

        var url = API_URL + serieName;
        var json = apiConsumer.getJsonByUrl(url);
        var serieData = dataConverter.convert(json, SerieData.class);
        System.out.println(serieData);

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= serieData.totalSeasons(); i++) {
            url = API_URL + serieName + "&Season=" + i;
            json = apiConsumer.getJsonByUrl(url);
            var seasonData = dataConverter.convert(json, SeasonData.class);
            seasons.add(seasonData);
        }

        /*
        Using for loop:

        for (SeasonData season : seasons) {
            for (EpisodeData episode : season.episodes()) {
                System.out.println(episode.title());
            }
         */
        /*
        Using for-each with lambda:

        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));
        */

        List<EpisodeData> episodesList = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

        episodesList.stream()
                .filter(e -> e.rating() != null && !e.rating().equals("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
