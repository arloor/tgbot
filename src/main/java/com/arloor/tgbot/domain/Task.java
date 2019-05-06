package com.arloor.tgbot.domain;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class Task implements Runnable{
    protected Update update;
    protected AbsSender sender;

    public Task(Update update,AbsSender sender) {
        this.update = update;
        this.sender = sender;
    }
}
