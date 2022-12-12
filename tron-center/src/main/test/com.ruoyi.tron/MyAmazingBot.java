package com.ruoyi.tron;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyAmazingBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "myamazingbot";
    }

    @Override
    public String getBotToken() {
        return "5745457029:AAGiQ3ksIDnlY0oLFaoG_z1GGMlXyJg1iOE";
    }

    @Override
    public void onUpdateReceived(Update update) {
// We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("后台授权处理：" + update.getMessage().getText());

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sayHello() {
        SendMessage snd = new SendMessage();
        snd.setText("Hello!");
        snd.setChatId(123L);

        try {
            // We want to test that we actually sent out this message with the contents "Hello!"
            execute(snd);
        } catch (TelegramApiException e) {
        }
    }
}
