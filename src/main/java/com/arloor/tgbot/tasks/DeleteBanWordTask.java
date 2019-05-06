package com.arloor.tgbot.tasks;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.domain.Task;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DeleteBanWordTask  extends Task {

    public DeleteBanWordTask(Update update, AbsSender sender) {
        super(update, sender);
    }

    @Override
    public void run() {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            //删除广告等
            if (ban(text)) {
                DeleteMessage delete = new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId());
                try {
                    sender.execute(delete);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean ban(String text) {
        for (String banWord:Config.instacne.getBanWords()
        ) {
            if(text.contains(banWord)){
                return true;
            }
        }
        return false;
    }
}
