package com.arloor.tgbot.tasks;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.domain.DelayDeleteMessage;
import com.arloor.tgbot.domain.DelayDeletor;
import com.arloor.tgbot.domain.Task;
import com.arloor.tgbot.workers.Worker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ReplyTask extends Task {

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

    private static int deleteDelay=Config.instacne.getDeleteDelay()*1000;
    private static InlineKeyboardMarkup inlineKeyboardMarkup;

    static {

        List<List<InlineKeyboardButton>> buttons=new LinkedList<>();

        List<InlineKeyboardButton> line1=new LinkedList<>();
        line1.add(new InlineKeyboardButton().setText("挖矿题库").setUrl("http://cao.chat/mines/"));
        line1.add(new InlineKeyboardButton().setText("分红说明").setUrl("http://cao.chat/dividends/"));


        buttons.add(line1);

        List<InlineKeyboardButton> line2=new LinkedList<>();
        line2.add(new InlineKeyboardButton().setText("挖矿机制").setUrl("http://cao.chat/mine/"));
        line2.add(new InlineKeyboardButton().setText("邀请机制").setUrl("http://cao.chat/invite/"));
        line2.add(new InlineKeyboardButton().setText("公社攻略").setUrl("http://caoliu.one/guide/"));
        buttons.add(line2);

        List<InlineKeyboardButton> line3=new LinkedList<>();
        line3.add(new InlineKeyboardButton().setText("界面介绍").setUrl("http://caoliu.one/caochat1/"));
        line3.add(new InlineKeyboardButton().setText("微信客服").setUrl("http://caoliu.one/weixin/"));
        line3.add(new InlineKeyboardButton().setText("官方承兑商").setUrl("http://caoliu.one/ex/"));
        buttons.add(line3);

        List<InlineKeyboardButton> line4=new LinkedList<>();
        line4.add(new InlineKeyboardButton().setText("问题回答").setUrl("http://cao.chat/qa/"));
        line4.add(new InlineKeyboardButton().setText("官方公告").setUrl("http://caoliu.one/blog/"));
        line4.add(new InlineKeyboardButton().setText("BUG建议").setUrl("https://wj.qq.com/s2/3258407/cc9b/"));
        buttons.add(line4);

        inlineKeyboardMarkup=new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
    }


    public ReplyTask(Update update, AbsSender sender) {
        super(update, sender);
    }

    @Override
    public void run() {
        //如果是群聊，则默认删除消息
        boolean toDelete=update.getMessage().getChat().isGroupChat()||update.getMessage().getChat().isSuperGroupChat();

        String temp;
        String text=update.getMessage().getText();



        //群组内只处理以 / 开头的东西
        if(text.startsWith("/")||update.getMessage().getChat().isUserChat()){
            if(text.startsWith("/qa")){
                temp=QA;
            }else if(text.startsWith("/help")){
                SendMessage tt = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("感谢使用本助手\n我知道的都在下面哦！\n更有 *挖矿答题题库* 赠与君~")
                        .setReplyToMessageId(update.getMessage().getMessageId())
                        .setParseMode("Markdown")
                        .setReplyMarkup(inlineKeyboardMarkup)
                        ;
                try {
                    Message msg=sender.execute(tt); // Call method to send the message
                    DeleteMessage replyToDelete=new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(msg.getMessageId());
                    DeleteMessage rawToDelete=new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId());
                    long timeStamp=System.currentTimeMillis();
                    DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp+deleteDelay,rawToDelete,sender));
                    DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp+deleteDelay ,replyToDelete,sender));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }else if(text.startsWith("/invite")){
                temp=invite;
            }else if(text.startsWith("/mines")){
                temp=mines;
            }else if(text.startsWith("/dividends")){
                temp=dividends;
            }else if(text.startsWith("/caochat1")){
                temp=caochat1;
            }else if(text.startsWith("/caochat2")){
                temp=caochat2;
            }else if(text.startsWith("/mine")){
                temp=mine;
            }else if(text.startsWith("/guide")){
                temp=guide;
            }else if(text.startsWith("/caochat4")){
                temp=caochat4;
            }else if(text.startsWith("/blog")){
                temp=blog;
            }else if(text.startsWith("/ex")){
                temp=ex;
            }else if(text.startsWith("/weixin")){
                temp=weixin;
            }else if(text.startsWith("/bug")){
                temp=bug;
            }else if(text.startsWith("/caochat3")){
                temp=caochat3;
            }else if(text.startsWith("/start")){
                temp="我是机器人“草信助手”，欢迎使用！\n试试输入“/help”吧🐷🐷";
            }else temp="试试输入“/help”吧🐷🐷";
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(temp);
            try {
                Message msg=sender.execute(message); // Call method to send the message
                //如果要删除，则删除
                if(toDelete){
                    DeleteMessage replyToDelete=new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(msg.getMessageId());
                    DeleteMessage rawToDelete=new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId());
                    long timeStamp=System.currentTimeMillis();
                    DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp+deleteDelay,rawToDelete,sender));
                    DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp+deleteDelay ,replyToDelete,sender));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }


}
