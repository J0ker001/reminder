package ru.anton.reminder.service;

public interface TelegramService {
    String sendTelegramMessage(String chatId, String title, String description);
}
