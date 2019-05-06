package com.arloor.tgbot;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.tasks.DeleteBanWordTask;
import com.arloor.tgbot.tasks.ReplyTask;
import com.arloor.tgbot.tasks.WelcomeTask;
import com.arloor.tgbot.workers.Worker;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ImBot extends TelegramLongPollingBot {

    private static String mines="草信-挖矿题库 http://cao.chat/mines/";
    private static String guide="草流-公社攻略 http://caoliu.one/guide/";
    private static String QA="草信-问题回答 http://cao.chat/qa/";
    private static String invite="草信-邀请机制 http://cao.chat/invite/";
    private static String mine="草信-挖矿机制 http://cao.chat/mine/";
    private static String dividends="草信-分红说明 http://cao.chat/dividends/";
    private static String caochat1="草信-界面介绍 http://caoliu.one/caochat1/";
    private static String caochat2 ="草信-建群攻略 http://caoliu.one/caochat2/";
    private static String caochat3="草信-置顶删除 http://caoliu.one/caochat3/";
    private static String caochat4="草信-缓存清理 http://caoliu.one/caochat4/";
    private static String blog="草流-官方公告 http://caoliu.one/blog/";
    private static String ex="草流-官方承兑商 http://caoliu.one/ex/";
    private static String weixin="草流-微信客服 http://caoliu.one/weixin/";
    private static String bug="草信-BUG建议 https://wj.qq.com/s2/3258407/cc9b/";





    public ImBot(DefaultBotOptions options) {
        super(options);
    }

    public void initBot(){
        startDeleteTask();
    }

    public void startDeleteTask(){
        new Thread(()->{
//            while(true){
//                try {
//                    Thread.sleep(5000);
//                    long current=System.currentTimeMillis();
//                    ToDeleteMessage toDeleteMessage;
//                    while((toDeleteMessage=queue.peek())!=null&&toDeleteMessage.timestamp<current-60000){
//                        toDeleteMessage=queue.poll();
//                        if(toDeleteMessage!=null){
//                            execute(toDeleteMessage.msg);
//                        }
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
        }).start();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage()&&!update.getMessage().getNewChatMembers().isEmpty()){
            Worker.handler(new WelcomeTask(update,this));
        }


        if (update.hasMessage() && update.getMessage().hasText()) {
            Worker.handler(new DeleteBanWordTask(update,this));
            Worker.handler(new ReplyTask(update,this));
            }
    }

    @Override
    public String getBotUsername() {
        return "CaochatBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return Config.instacne.getBotToken();
    }
}
