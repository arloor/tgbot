package com.arloor.tgbot;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.domain.DelayDeleteMessage;
import com.arloor.tgbot.domain.DelayDeletor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class ImBot extends TelegramLongPollingBot {
    private String targetChannel="@KickHimOut";
    private static Logger logger= LoggerFactory.getLogger(ImBot.class);

    public ImBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info(update.toString());

        if(update.hasMessage()){
            Message rawMessage=update.getMessage();
            User fromUser=rawMessage.getForwardFrom();
            if(fromUser!=null) {
                String nickName = fromUser.getFirstName() + (fromUser.getLastName() == null ? "" : " " + fromUser.getLastName());
                String userLine = "<a href=\"tg://user?id=" + fromUser.getId() + "\">" + nickName + "</a>";


                if(rawMessage.hasDocument()){
                    SendDocument doc=new SendDocument()
                            .setDocument(rawMessage.getDocument().getFileId())
                            .setCaption((userLine+(rawMessage.getCaption()==null?" 发布":":\n"+rawMessage.getCaption())))
                            .setParseMode("HTML")
                            .setChatId(targetChannel);
                    try {
                        execute(doc);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                if(rawMessage.hasVideo()){
                    SendVideo video=new SendVideo()
                            .setVideo(rawMessage.getVideo().getFileId())
                            .setCaption((userLine+(rawMessage.getCaption()==null?" 发布":":\n"+rawMessage.getCaption())))
                            .setParseMode("HTML")
                            .setChatId(targetChannel);
                    try {
                        execute(video);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                if(rawMessage.hasPhoto()){
                    SendPhoto photo=new SendPhoto()
                            .setPhoto(rawMessage.getPhoto().get(0).getFileId())
                            .setCaption((userLine+(rawMessage.getCaption()==null?" 发布":":\n"+rawMessage.getCaption())))
                            .setParseMode("HTML")
                            .setChatId(targetChannel);
                    try {
                        execute(photo);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                if(rawMessage.hasText()){
                    SendMessage RawText = new SendMessage()
                            .setChatId(targetChannel)
                            .setParseMode("HTML")
                            .setText(userLine+":\n"+rawMessage.getText());

                    try {
                        execute(RawText);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(rawMessage.hasSticker()){
                    SendSticker video=new SendSticker()
                            .setSticker(rawMessage.getSticker().getFileId())
                            .setChatId(targetChannel);
                    try {
                        execute(video);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                SendMessage message=new SendMessage()
                        .setReplyToMessageId(rawMessage.getMessageId())
                        .setChatId(rawMessage.getChatId())
                        .setText("转发完毕🤣!");
                try {
                    Message result=execute(message);
                    DeleteMessage deleteMessage=new DeleteMessage()
                            .setMessageId(result.getMessageId()).setChatId(message.getChatId());
                    DelayDeletor.addToDelete(new DelayDeleteMessage(10,deleteMessage,this));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }else{
                SendMessage message=new SendMessage()
                        .setReplyToMessageId(rawMessage.getMessageId())
                        .setChatId(rawMessage.getChatId())
                        .setText("转发内容到本Bot，让更多人知道吧🤣🤣🤣频道地址 "+targetChannel);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "KickHimOutBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return Config.instacne.getBotToken();
    }


}
