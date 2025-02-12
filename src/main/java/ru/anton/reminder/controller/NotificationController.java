package ru.anton.reminder.controller;

import org.springframework.web.bind.annotation.*;
import ru.anton.reminder.service.EmailService;
import ru.anton.reminder.service.NotificationService;
import ru.anton.reminder.service.TelegramService;

@RestController
@RequestMapping("/domain/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final TelegramService telegramService;
    private final EmailService emailService;

    public NotificationController(NotificationService notificationService, TelegramService telegramService,
                                  EmailService emailService) {
        this.notificationService = notificationService;
        this.telegramService = telegramService;
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String triggerNotifications() {
        notificationService.checkAndSendNotifications();
        return "Notifications have been sent!";
    }

    @GetMapping("/sendEmail")
    public String sendEmail(String to, String subject, String text) {
       return emailService.sendEmail(to, subject, text);

    }

    @GetMapping("/sendTelegram")
    public String sendTelegramMessage(@RequestParam String chatId,
                                      @RequestParam String title,
                                      @RequestParam String description) {
       return telegramService.sendTelegramMessage(chatId, title, description);
    }
}
