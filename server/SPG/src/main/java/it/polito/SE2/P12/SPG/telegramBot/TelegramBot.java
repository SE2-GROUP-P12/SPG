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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final SpgUserService userService;
    private final String botName = "SPG_p12";
    private List<String> unregisteredUsers = new ArrayList<String>();

    public String getBotName() {
        return botName;
    }

    @Autowired
    public TelegramBot(SpgUserService userService) {
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {

        String msg = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.findUserByChatId(chatId);

        if (user == null) {

            if (!unregisteredUsers.contains(chatId)) {
                sendMessage(chatId, "Welcome to SPG_p12 telegram bot!\nPlease enter your email for registration.");
                unregisteredUsers.add(chatId);
            }

            else {
                if(!userService.setChatIdToUser(msg, chatId)){
                    sendMessage(chatId, "User not found, try again.");
                }
                else{
                    sendMessage(chatId, "Success!");
                    unregisteredUsers.remove(chatId);
                }
            }
        } else {
            sendMessage(chatId,"User registered with email: "+user.getEmail());
        }

    }

    public void notifyCustomers(String productName){
        for(User user:userService.findAllUsers()){
            if(!user.getChatId().equals("")&& user.getChatId()!=null){
                this.sendMessage(user.getChatId(), productName + " is now available! Go check it out!");
            }
        }
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String risposta = message;
        sendMessage.setText(risposta);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());

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
//
