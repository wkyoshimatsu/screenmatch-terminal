package me.wky.screenmatch_terminal;

import me.wky.screenmatch_terminal.service.APIConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
	}
}
