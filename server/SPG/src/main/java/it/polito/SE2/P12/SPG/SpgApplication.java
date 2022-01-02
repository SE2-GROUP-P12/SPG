package it.polito.SE2.P12.SPG;

import it.polito.SE2.P12.SPG.telegramBot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class SpgApplication {

	public static void main(String[] args) {
		System.err.println("~~~ SPG server v0.0.4 ~~~");
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(new TelegramBot());
		} catch (TelegramApiException e) {

		}

		SpringApplication.run(SpgApplication.class, args);
	}

}
