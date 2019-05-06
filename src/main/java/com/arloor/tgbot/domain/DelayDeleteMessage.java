package com.arloor.tgbot.domain;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayDeleteMessage implements Delayed {
    private  long expire;
    private DeleteMessage msg;
    private AbsSender sender;

    public DelayDeleteMessage(long expire, DeleteMessage msg,AbsSender sender) {
        this.expire = expire;
        this.msg = msg;
        this.sender = sender;
    }

    public AbsSender getSender() {
        return sender;
    }

    public DeleteMessage getMsg() {
        return msg;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) -o.getDelay(TimeUnit.MILLISECONDS));
    }
}
