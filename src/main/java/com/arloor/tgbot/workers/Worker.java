package com.arloor.tgbot.workers;

import com.arloor.tgbot.domain.DelayDeleteMessage;
import com.arloor.tgbot.domain.Task;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker {
    private final static ExecutorService pool= Executors.newFixedThreadPool(4);
    public static DelayQueue<DelayDeleteMessage> toDelete=new DelayQueue<>();

    //延迟删除message
    static {
        pool.execute(()->{
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
        });
    }


    public static void handler(Task task) {
        pool.execute(task);
    }
}
