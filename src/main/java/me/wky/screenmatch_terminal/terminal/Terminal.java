package me.wky.screenmatch_terminal.terminal;

import me.wky.screenmatch_terminal.model.Episode;
import me.wky.screenmatch_terminal.model.EpisodeData;
import me.wky.screenmatch_terminal.model.SeasonData;
import me.wky.screenmatch_terminal.model.SerieData;
import me.wky.screenmatch_terminal.service.APIConsumer;
import me.wky.screenmatch_terminal.service.DataConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Terminal {
    private final String OMDBAPI_KEY= System.getenv("OMDBAPI_KEY");

    private final String API_URL = "http://www.omdbapi.com/?apikey=" + OMDBAPI_KEY + "&t=";

    private Scanner scanner = new Scanner(System.in);

    private APIConsumer apiConsumer = new APIConsumer();

    private DataConverter dataConverter = new DataConverter();

    public List<SeasonData> searchSerieByName() {
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
        return seasons;
    }

    public void printTopFiveEpisodes(List<SeasonData> seasons) {
        System.out.println("\nTop 5 episódios com melhor avaliação:");

        List<EpisodeData> episodesList = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());

        episodesList.stream()
                .filter(e -> e.rating() != null && !e.rating().equals("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);
    }

    public void printTopFiveEpisodesWithNewConstructor(List<Episode> episodes) {
        System.out.println("\nTop 5 episódios com melhor avaliação (usando novo constutor):");

        episodes.stream()
                .sorted(Comparator.comparing(Episode::getRating).reversed())
                .limit(5)
                .forEach(System.out::println);
    }

    public void printSearchEpisodesFromYearOnwardsResults(List<Episode> episodes){
        System.out.println("\nA partir de qual ano deseja ver os episódios?");
        var year = scanner.nextInt();

        LocalDate searchDate = LocalDate.of(year, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodes.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getSeason() +
                                ", Episódio: " + e.getTitle() +
                                ", Data de lançamento: " + e.getReleaseDate().format(formatter))
                );
    }

    public void printSeasonByEpisodeExcerptResults(List<Episode> episodes){
        System.out.println("\nBusca por qual temporada o episódio pertence\nDigite o trecho do título do episódio");
        scanner.nextLine(); //Usado para limpar o buffer
        var titleExcerpt = scanner.nextLine().toLowerCase();
        System.out.println("Buscando por: " + titleExcerpt);
        Optional<Episode> episodeSearchedByTitle = episodes.stream()
                .filter(e -> e.getTitle().toLowerCase().contains(titleExcerpt))
                .findFirst();

        if (episodeSearchedByTitle.isPresent()) {
            System.out.println("O episódio " + episodeSearchedByTitle.get().getTitle() +
                    " pertence à temporada " + episodeSearchedByTitle.get().getSeason());
        } else {
            System.out.println("Episódio não encontrado");
        }
    }

    public void printSeasonsAverages(List<Episode> episodes){
        Map<Integer, Double> averageRatingBySeason = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));

        System.out.println("\nMédia de avaliação por temporada:\n" + averageRatingBySeason);
    }

    public void printSerieStatistics(List<Episode> episodes){
        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        //System.out.println("\nEstatísticas de avaliação:\n" + est);
        System.out.println("Quantidade de avaliações:" + est.getCount());
        System.out.println("Média de avaliação:" + est.getAverage());
        System.out.println("Menor avaliação: " + est.getMin());
        System.out.println("Maior avaliação:" + est.getMax());
    }

    public void printMenu() {
        List<SeasonData> seasons = searchSerieByName();

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(ed -> new Episode(s.season(), ed)))
                .collect(Collectors.toList());

        printTopFiveEpisodes(seasons);

        printTopFiveEpisodesWithNewConstructor(episodes);

        printSearchEpisodesFromYearOnwardsResults(episodes);

        printSeasonByEpisodeExcerptResults(episodes);

        printSeasonsAverages(episodes);

        printSerieStatistics(episodes);





    }
}
