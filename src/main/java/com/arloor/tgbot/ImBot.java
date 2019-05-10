package com.arloor.tgbot;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.tasks.DeleteBanWordTask;
import com.arloor.tgbot.tasks.ReplyTask;
import com.arloor.tgbot.tasks.WelcomeTask;
import com.arloor.tgbot.workers.Worker;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ImBot extends TelegramLongPollingBot {
    public ImBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage()&&!update.getMessage().getNewChatMembers().isEmpty()){
            Worker.handler(new WelcomeTask(update,this));
        }


        if (update.hasMessage() && update.getMessage().hasText()) {
            Worker.handler(new DeleteBanWordTask(update,this));
            Worker.handler(new ReplyTask(update,this));
            }
    }

    @Override
    public String getBotUsername() {
        return "CaochatBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return Config.instacne.getBotToken();
    }
}
