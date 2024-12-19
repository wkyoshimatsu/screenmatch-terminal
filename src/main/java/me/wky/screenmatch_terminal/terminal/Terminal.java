package me.wky.screenmatch_terminal.terminal;

import me.wky.screenmatch_terminal.model.*;
import me.wky.screenmatch_terminal.repository.SeriesRepository;
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
    private List<SeriesData> seriesDataList = new ArrayList<>();
    private SeriesRepository seriesRepository;


    public Terminal(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public void printMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                
                Bem vindo ao Screenmatch!
                
                Escolha uma opção:
                1 - Buscar série
                2 - Buscar episódio
                3 - Listar séries buscadas
                
                0 - Sair
                """;

            System.out.println(menu);

            option = scanner.nextInt();
            scanner.nextLine(); //Usado para limpar o buffer

            switch (option) {
                case 1 -> searchSeriesTitleExcerpt();
                case 2 -> searchEpisodesBySeriesTitleExcerpt();
                case 3 -> printSearchedSeries();
                case 0 -> System.exit(0);
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void searchSeriesTitleExcerpt(){
        SeriesData seriesData = getSeriesData();
        //seriesDataList.add(seriesData);
        seriesRepository.save(new Series(seriesData));
        System.out.println(seriesData);
    }

    private SeriesData getSeriesData() {
        System.out.println("Digite o nome da série desejada:");
        var seriesName = scanner.nextLine().toLowerCase().replace(" ", "+");
        var json = apiConsumer.getJsonByUrl(API_URL + seriesName);
        return dataConverter.convert(json, SeriesData.class);
    }

    private void searchEpisodesBySeriesTitleExcerpt(){
        SeriesData seriesData = getSeriesData();

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            var json = apiConsumer.getJsonByUrl(API_URL + seriesData.title() + "&Season=" + i);
            var seasonData = dataConverter.convert(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }

    private void printSearchedSeries(){
        //List<Series> seriesList = seriesRepository.findAll();
        List<Series> seriesList = seriesDataList.stream()
                .map(Series::new)
                .collect(Collectors.toList());
        seriesList.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

//    public void printTopFiveEpisodes(List<SeasonData> seasons) {
//        System.out.println("\nTop 5 episódios com melhor avaliação:");
//
//        List<EpisodeData> episodesList = seasons.stream()
//                .flatMap(s -> s.episodes().stream())
//                .collect(Collectors.toList());
//
//        episodesList.stream()
//                .filter(e -> e.rating() != null && !e.rating().equals("N/A"))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .limit(5)
//                .forEach(System.out::println);
//    }
//
//    public void printTopFiveEpisodesWithNewConstructor(List<Episode> episodes) {
//        System.out.println("\nTop 5 episódios com melhor avaliação (usando novo constutor):");
//
//        episodes.stream()
//                .sorted(Comparator.comparing(Episode::getRating).reversed())
//                .limit(5)
//                .forEach(System.out::println);
//    }
//
//    public void printSearchEpisodesFromYearOnwardsResults(List<Episode> episodes){
//        System.out.println("\nA partir de qual ano deseja ver os episódios?");
//        var year = scanner.nextInt();
//
//        LocalDate searchDate = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getSeason() +
//                                ", Episódio: " + e.getTitle() +
//                                ", Data de lançamento: " + e.getReleaseDate().format(formatter))
//                );
//    }
//
//    public void printSeasonByEpisodeExcerptResults(List<Episode> episodes){
//        System.out.println("\nBusca por qual temporada o episódio pertence\nDigite o trecho do título do episódio");
//        scanner.nextLine(); //Usado para limpar o buffer
//        var titleExcerpt = scanner.nextLine().toLowerCase();
//        System.out.println("Buscando por: " + titleExcerpt);
//        Optional<Episode> episodeSearchedByTitle = episodes.stream()
//                .filter(e -> e.getTitle().toLowerCase().contains(titleExcerpt))
//                .findFirst();
//
//        if (episodeSearchedByTitle.isPresent()) {
//            System.out.println("O episódio " + episodeSearchedByTitle.get().getTitle() +
//                    " pertence à temporada " + episodeSearchedByTitle.get().getSeason());
//        } else {
//            System.out.println("Episódio não encontrado");
//        }
//    }
//
//    public void printSeasonsAverages(List<Episode> episodes){
//        Map<Integer, Double> averageRatingBySeason = episodes.stream()
//                .filter(e -> e.getRating() > 0.0)
//                .collect(Collectors.groupingBy(Episode::getSeason,
//                        Collectors.averagingDouble(Episode::getRating)));
//
//        System.out.println("\nMédia de avaliação por temporada:\n" + averageRatingBySeason);
//    }
//
//    public void printSerieStatistics(List<Episode> episodes){
//        DoubleSummaryStatistics est = episodes.stream()
//                .filter(e -> e.getRating() > 0.0)
//                .collect(Collectors.summarizingDouble(Episode::getRating));
//        //System.out.println("\nEstatísticas de avaliação:\n" + est);
//        System.out.println("Quantidade de avaliações:" + est.getCount());
//        System.out.println("Média de avaliação:" + est.getAverage());
//        System.out.println("Menor avaliação: " + est.getMin());
//        System.out.println("Maior avaliação:" + est.getMax());
//    }
}
