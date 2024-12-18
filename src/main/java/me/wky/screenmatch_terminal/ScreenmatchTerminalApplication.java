package me.wky.screenmatch_terminal;

import me.wky.screenmatch_terminal.terminal.Terminal;
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
		Terminal terminal = new Terminal();
		terminal.printMenu();
	}
}
