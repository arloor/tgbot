package com.arloor.tgbot.domain;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class Config {
    private String botToken;
    private String thankForUse;
    private int botID;
    private int deleteDelay;
    private List<String> banWords;
    public final static Config instacne=init();

    private Config() {
    }

    private static Config init() {

        File file = new File("config.json");
        if (!file.exists()) {
            System.out.println("Error: config.json doesn't exists");
            System.exit(-1);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Files.copy(file.toPath(), outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Config instance=JSON.parseObject(outputStream.toString(),Config.class);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public int getDeleteDelay() {
        return deleteDelay;
    }

    public void setDeleteDelay(int deleteDelay) {
        this.deleteDelay = deleteDelay;
    }

    public String getThankForUse() {
        return thankForUse;
    }

    public void setThankForUse(String thankForUse) {
        this.thankForUse = thankForUse;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public int getBotID() {
        return botID;
    }

    public void setBotID(int botID) {
        this.botID = botID;
    }

    public List<String> getBanWords() {
        return banWords;
    }

    public void setBanWords(List<String> banWords) {
        this.banWords = banWords;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
