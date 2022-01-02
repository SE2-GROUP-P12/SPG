package it.polito.SE2.P12.SPG.telegramBot;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;



public class TelegramBot extends TelegramLongPollingBot {

    //@Autowired
    private SpgUserService userService=null;
    private final String botName= "SPG_p12";

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
        //User user = userService.findUserByChatId(chatId);
        String user = null;
        if(user==null){
            //if (msg == "/start"){
                sendMessage(chatId, "Welcome to SPG_p12 telegram bot!\nPlease enter your email for registration.");

            /*else {
                if(!userService.setChatIdToUser(msg, chatId))sendMessage(chatId, "User not found, try again.");
                else sendMessage(chatId, "Success!");
            }*/
        }
        else{
            //sendMessage(chatId,user.getEmail());
        }

    }

    public void sendMessage(String chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String risposta=message;
        sendMessage.setText(risposta);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();

        }
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
