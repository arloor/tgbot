package com.arloor.tgbot.tasks;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.domain.Task;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class WelcomeTask extends Task {
    String format="【%s %s】 欢迎来到 %s ！\n输入“/help”试试吧";

    public WelcomeTask(Update update, AbsSender sender) {
        super(update, sender);
    }

    @Override
    public void run() {
        if (update.hasMessage()&&!update.getMessage().getNewChatMembers().isEmpty()){
            List<User> users=update.getMessage().getNewChatMembers();
            for (User user:users
            ) {
                if(user.getId()!= Config.instacne.getBotID()){
                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setReplyToMessageId(update.getMessage().getMessageId())
                            .setText(String.format(format,user.getFirstName(),user.getLastName()==null?"":user.getLastName(),update.getMessage().getChat().getTitle()));
                    try {
                        sender.execute(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else {
                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText(Config.instacne.getThankForUse());
                    try {
                        sender.execute(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
