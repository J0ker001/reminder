package ru.anton.reminder.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.anton.reminder.service.TelegramService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramServiceImpl implements TelegramService {
    @Value("${telegram.api-url}")
    private String TELEGRAM_API_URL;

    @Override
    public String sendTelegramMessage(String chatId, String title, String description) {
        if (chatId == null) return "incorrect chatId";
        String message = "*" + title + "*\n" + description;
        String url = TELEGRAM_API_URL + "?chat_id=" + chatId + "&text=" + escapeMarkdown(message) + "&parse_mode=Markdown";

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(url, String.class);
            log.info("Telegram {} reminder sent ", chatId);
            return "Telegram " + chatId + " reminder sent";
        } catch (Exception e) {
            log.error("Error sending reminder in telegram {}: {}", chatId, e.getMessage());
            return "Error sending reminder in telegram " + chatId;
        }
    }

    private static String escapeMarkdown(String message) {
        return message.replace("*", "\\*")
                .replace("_", "\\_")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("`", "\\`")
                .replace("~", "\\~")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=");
    }
}
