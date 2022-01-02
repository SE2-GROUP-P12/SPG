package it.polito.SE2.P12.SPG.telegramBot;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.TelegramBotsApi;


public class TelegramBot extends TelegramLongPollingBot {

    private SpgUserService userService;
    private final String botName= "SPG_p12";



    public TelegramBot(SpgUserService userService) {
        this.userService = userService;
    }



    public String getBotName() {
        return botName;
    }

    public SpgUserService getUserService() {
        return userService;
    }

    public void setUserService(SpgUserService userService) {
        this.userService = userService;
    }



    @Override
    public void onUpdateReceived(Update update) {
        String msg = update.getMessage().getText();
        String chatId=update.getMessage().getChatId().toString();
        User user = userService.findUserByChatId(chatId);
        if(user==null){
            if (msg == "/start"){
                sendMessage(chatId, "Welcome to SPG_p12 telegram bot!\nPlease enter your email for registration.");
            }
            else {
                if(!userService.setChatIdToUser(msg, chatId))sendMessage(chatId, "User not found, try again.");
                else sendMessage(chatId, "Success!");
            }
        }
        else{
            sendMessage(chatId,user.getEmail());
        }

    }

    public void sendMessage(String chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String risposta=message;
        sendMessage.setText(risposta);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return "5097508665:AAEw--9_r39mbMgG5_pqQPbn2aGJuSAzl_Y";
    }
}
