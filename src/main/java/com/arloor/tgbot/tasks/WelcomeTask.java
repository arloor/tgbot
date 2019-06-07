package com.arloor.tgbot.tasks;

import com.arloor.tgbot.domain.Config;
import com.arloor.tgbot.domain.DelayDeleteMessage;
import com.arloor.tgbot.domain.DelayDeletor;
import com.arloor.tgbot.domain.Task;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;

import static com.arloor.tgbot.tasks.ReplyTask.inlineKeyboardMarkup;


public class WelcomeTask extends Task {
    String format="【%s %s】 欢迎来到 %s ！\n请在1分钟内点击 *我不是机器人* 获取发言权力\n然后输入“/help”学习一下吧";
    private static int deleteDelay = Config.instacne.getDeleteDelay() * 1000;

    public static InlineKeyboardMarkup notBotKeyboard=init();

    private static InlineKeyboardMarkup init() {

        List<List<InlineKeyboardButton>> buttons = new LinkedList<>();

        List<InlineKeyboardButton> line1 = new LinkedList<>();
        line1.add(new InlineKeyboardButton().setText("我不是机器人").setCallbackData("notBot"));
        buttons.add(line1);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return  inlineKeyboardMarkup;
    }

    public WelcomeTask(Update update, AbsSender sender) {
        super(update, sender);
    }

    @Override
    public void run() {
        if (update.hasMessage()&&!update.getMessage().getNewChatMembers().isEmpty()){
            List<User> users=update.getMessage().getNewChatMembers();
            for (User user:users
            ) {
                if(user.getId()!= Config.instacne.getBotID()){
                    RestrictChatMember jinyan=new RestrictChatMember();
                    jinyan.setCanSendMediaMessages(false);
                    jinyan.setCanAddWebPagePreviews(false);
                    jinyan.setCanSendMessages(false);
                    jinyan.setCanSendOtherMessages(false);
                    jinyan.setChatId(update.getMessage().getChatId());
                    jinyan.setUserId(update.getMessage().getFrom().getId());

                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setReplyToMessageId(update.getMessage().getMessageId())
                            .setParseMode("Markdown")
                            .setReplyMarkup(notBotKeyboard)
                            .setText(String.format(format,user.getFirstName(),user.getLastName()==null?"":user.getLastName(),update.getMessage().getChat().getTitle()));
                    try {
                        sender.execute(jinyan);
                        Message msg=sender.execute(message); // Call method to send the message

                        DeleteMessage replyToDelete = new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(msg.getMessageId());
                        DeleteMessage rawToDelete = new DeleteMessage().setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId());
                        long timeStamp = System.currentTimeMillis();
                        DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp + deleteDelay, rawToDelete, sender));
                        DelayDeletor.addToDelete(new DelayDeleteMessage(timeStamp + deleteDelay, replyToDelete, sender));

                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else {
                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setReplyMarkup(inlineKeyboardMarkup)
                            .setText(Config.instacne.getThankForUse());
                    try {
                        sender.execute(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
