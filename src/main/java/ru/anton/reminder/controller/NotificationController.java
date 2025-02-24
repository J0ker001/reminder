package ru.anton.reminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anton.reminder.dtos.infoRequest.InfoRequestSendTelegramDTO;
import ru.anton.reminder.dtos.infoRequest.infoRequestSendEmailDTO;
import ru.anton.reminder.service.EmailService;
import ru.anton.reminder.service.NotificationService;
import ru.anton.reminder.service.TelegramService;

@RestController
@RequestMapping("/domain/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final TelegramService telegramService;
    private final EmailService emailService;

    @PostMapping("/send")
    public String triggerNotifications() {
        notificationService.checkAndSendNotifications();
        return "Notifications have been sent!";
    }

    @GetMapping("/sendEmail")
    public void sendEmail(@RequestBody infoRequestSendEmailDTO info) {
        emailService.sendEmail(info.getTo(), info.getSubject(), info.getText());
    }

    @GetMapping("/sendTelegram")
    public String sendTelegramMessage(@RequestBody InfoRequestSendTelegramDTO info) {
        return telegramService.sendTelegramMessage(info.getChatId(), info.getTitle(), info.getDescription());
    }
}
