package com.arloor.tgbot.workers;

import com.arloor.tgbot.domain.DelayDeleteMessage;
import com.arloor.tgbot.domain.Task;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker {
    private final static ExecutorService pool= Executors.newFixedThreadPool(4);



    public static void handler(Task task) {
        pool.execute(task);
    }
}
