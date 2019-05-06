package com.arloor.tgbot;

import com.arloor.tgbot.domain.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;



public class Bootstrap {
    private final static String PROXY_HOST = "127.0.0.1" /* proxy host */;
    private final static Integer PROXY_PORT = 1080 /* proxy port */;
    private static final Logger logger= LoggerFactory.getLogger(Bootstrap.class);

//    public final static Config config=Config.init();

    public static void main(String[] args) throws TelegramApiRequestException {
        logger.info("config: "+ Config.instacne);

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        //如果是开发环境，则设置代理
        if(args.length==1 &&args[0].equals("dev")){
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        }
        ImBot bot = new ImBot(botOptions);

        bot.initBot();
        botsApi.registerBot(bot);
    }
}
