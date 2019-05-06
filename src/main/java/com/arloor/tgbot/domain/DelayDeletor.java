package com.arloor.tgbot.domain;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.concurrent.DelayQueue;
public class DelayDeletor {

    public static DelayQueue<DelayDeleteMessage> toDelete=new DelayQueue<>();

    public static void addToDelete(DelayDeleteMessage delayDeleteMessage){
        toDelete.add(delayDeleteMessage);
    }

    //延迟删除message
    static {
        new Thread(()->{
            while (true){
                try {
                    DelayDeleteMessage delayDeleteMessage=toDelete.take();
                    delayDeleteMessage.getSender().execute(delayDeleteMessage.getMsg());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
