package it.polito.SE2.P12.SPG.telegramBot;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.security.RestAuthEntryPoint;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
public class TelegramBotService {
/*
    @Autowired
    private final SpgUserService userService;

    private TelegramBot telegramBot;

    public TelegramBotService(SpgUserService userService){
        this.userService = userService;
        this.startBot();
    }

    public TelegramBot startBot(){
        //ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        telegramBot = telegramBot();
        try {
            api.registerBot(telegramBot);
        } catch (TelegramApiRequestException e) {
            // gestione errore in registrazione
        }
        return telegramBot;
    }

    public TelegramBot telegramBot() {
        return new TelegramBot(this.userService);
    }
}
*/
}