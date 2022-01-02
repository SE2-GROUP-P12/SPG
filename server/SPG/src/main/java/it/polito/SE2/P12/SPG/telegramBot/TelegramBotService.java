package it.polito.SE2.P12.SPG.telegramBot;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Service
public class TelegramBotService {

    @Autowired
    private SpgUserService userService;
    private TelegramBot telegramBot;

    public TelegramBotService(){

        this.telegramBot = this.startBot();
    }

    public TelegramBot startBot(){
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        TelegramBot bot  = new TelegramBot(userService);
        try {
            api.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            // gestione errore in registrazione
        }
        return bot;
    }
}
