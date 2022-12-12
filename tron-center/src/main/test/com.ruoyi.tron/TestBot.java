package com.ruoyi.tron;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TestBot {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            MyAmazingBot bot = new MyAmazingBot();
            telegramBotsApi.registerBot(bot);
            bot.sayHello();


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
