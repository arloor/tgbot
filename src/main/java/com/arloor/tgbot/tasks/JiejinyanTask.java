package com.arloor.tgbot.tasks;

import com.alibaba.fastjson.JSONObject;
import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.domain.Task;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;

import static com.arloor.tgbot.tasks.ReplyTask.inlineKeyboardMarkup;


public class JiejinyanTask extends Task {
    public JiejinyanTask(Update update, AbsSender sender) {
        super(update, sender);
    }

    @Override
    public void run() {
        if(update.hasCallbackQuery()){
//            System.out.println(JSONObject.toJSON(update));
            CallbackQuery callback=update.getCallbackQuery();
            if(callback.getMessage().getReplyToMessage()==null){
                return;
            }
            long targetUserId=callback.getMessage().getReplyToMessage().getFrom().getId();
            long clickId=callback.getFrom().getId();
            if(clickId!=targetUserId){
                AnswerCallbackQuery notTargetUser=new AnswerCallbackQuery();
                notTargetUser.setText("您不是目标用户");
                notTargetUser.setCallbackQueryId(callback.getId());
                try {
                    sender.execute(notTargetUser);// Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }else{
                if(callback.getData().equals("notBot")){
                    RestrictChatMember jiejinyan=new RestrictChatMember();
                    jiejinyan.setCanSendMediaMessages(true);
                    jiejinyan.setCanAddWebPagePreviews(true);
                    jiejinyan.setCanSendMessages(true);
                    jiejinyan.setCanSendOtherMessages(true);
                    jiejinyan.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    jiejinyan.setUserId(update.getCallbackQuery().getFrom().getId());

//                    DeleteMessage replyToDelete = new DeleteMessage().setChatId(callback.getMessage().getChatId()).setMessageId(callback.getMessage().getMessageId());
                    try {
                        sender.execute(jiejinyan);// Call method to send the message
//                        sender.execute(replyToDelete);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
