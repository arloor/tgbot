package com.arloor.tgbot;

import com.arloor.tgbot.domain.Config;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class ImBot extends TelegramLongPollingBot {
    private String targetChannel="@KickHimOut";

    public ImBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {


        if(update.hasMessage()){
            Message rawMessage=update.getMessage();
            User fromUser=rawMessage.getForwardFrom();
            if(fromUser!=null) {
                String nickName = fromUser.getFirstName() + (fromUser.getLastName() == null ? "" : " " + fromUser.getLastName());
                String userLine = "<a href=\"tg://user?id=" + fromUser.getId() + "\">" + nickName + "</a>";

                if(rawMessage.hasPhoto()){
                    System.out.println(rawMessage);
                    SendPhoto photo=new SendPhoto()
                            .setPhoto(rawMessage.getPhoto().get(0).getFileId())
                            .setCaption((userLine+(rawMessage.getCaption()==null?"":":\n"+rawMessage.getCaption())))
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

                SendMessage message=new SendMessage()
                        .setReplyToMessageId(rawMessage.getMessageId())
                        .setChatId(rawMessage.getChatId())
                        .setText("转发完毕🤣!");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }else{
                SendMessage message=new SendMessage()
                        .setReplyToMessageId(rawMessage.getMessageId())
                        .setChatId(rawMessage.getChatId())
                        .setText("转发CNZZ到本Bot，让他出道吧🤣🤣🤣频道地址 "+targetChannel);
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
