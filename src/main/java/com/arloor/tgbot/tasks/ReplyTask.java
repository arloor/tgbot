package com.arloor.tgbot.tasks;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.domain.DelayDeleteMessage;
import com.arloor.tgbot.domain.DelayDeletor;
import com.arloor.tgbot.domain.Task;
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

public class ReplyTask extends Task {

    private static int deleteDelay = Config.instacne.getDeleteDelay() * 1000;
    private static InlineKeyboardMarkup inlineKeyboardMarkup;

    static {

        List<List<InlineKeyboardButton>> buttons = new LinkedList<>();

        List<InlineKeyboardButton> line1 = new LinkedList<>();
        line1.add(new InlineKeyboardButton().setText("挖矿题库").setUrl("http://cao.chat/mines/"));
        line1.add(new InlineKeyboardButton().setText("分红说明").setUrl("http://cao.chat/dividends/"));
        buttons.add(line1);

        List<InlineKeyboardButton> line2 = new LinkedList<>();
        line2.add(new InlineKeyboardButton().setText("挖矿机制").setUrl("http://cao.chat/mine/"));
        line2.add(new InlineKeyboardButton().setText("邀请机制").setUrl("http://cao.chat/invite/"));
        line2.add(new InlineKeyboardButton().setText("公社攻略").setUrl("http://caoliu.one/guide/"));
        buttons.add(line2);

        List<InlineKeyboardButton> line3 = new LinkedList<>();
        line3.add(new InlineKeyboardButton().setText("界面介绍").setUrl("http://caoliu.one/caochat1/"));
        line3.add(new InlineKeyboardButton().setText("微信客服").setUrl("http://caoliu.one/weixin/"));
        line3.add(new InlineKeyboardButton().setText("官方承兑商").setUrl("http://caoliu.one/ex/"));
        buttons.add(line3);

        List<InlineKeyboardButton> line4 = new LinkedList<>();
        line4.add(new InlineKeyboardButton().setText("问题回答").setUrl("http://cao.chat/qa/"));
        line4.add(new InlineKeyboardButton().setText("官方公告").setUrl("http://caoliu.one/blog/"));
        line4.add(new InlineKeyboardButton().setText("BUG建议").setUrl("https://wj.qq.com/s2/3258407/cc9b/"));
        buttons.add(line4);

        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
    }


    public ReplyTask(Update update, AbsSender sender) {
        super(update, sender);
    }

    @Override
    public void run() {
        //如果是群聊，则默认删除消息
        boolean toDelete = update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat();
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        String text = update.getMessage().getText();
        if (text.startsWith("/") || update.getMessage().getChat().isUserChat()||text.startsWith(Config.instacne.getBotName())) {
            if(text.startsWith(Config.instacne.getBotName())){
                text=text.substring(Config.instacne.getBotName().length()).trim();
            }
            if (text.startsWith("/help")) {
                message.setChatId(update.getMessage().getChatId())
                        .setText("感谢使用本助手\n我知道的都在下面哦！\n更有 *挖矿答题题库* 赠与君~")
                        .setReplyToMessageId(update.getMessage().getMessageId())
                        .setParseMode("Markdown")
                        .setReplyMarkup(inlineKeyboardMarkup);
            } else if (text.startsWith("/start")) {
                String temp = "我是机器人“草信官方助手”，欢迎使用！\n试试输入“/help”吧🐷🐷";
                message.setChatId(update.getMessage().getChatId())
                        .setText(temp);
            } else {
                String temp = "我不能理解哦，试试输入“/help”吧🐷🐷";
                message.setChatId(update.getMessage().getChatId())
                        .setText(temp);
            }

            try {
                Message msg = sender.execute(message); // Call method to send the message
                //如果要删除，则删除
                if (toDelete) {
                    DeleteMessage replyToDelete = new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(msg.getMessageId());
                    DeleteMessage rawToDelete = new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId());
                    long timeStamp = System.currentTimeMillis();
                    DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp + deleteDelay, rawToDelete, sender));
                    DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp + deleteDelay, replyToDelete, sender));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }


}
