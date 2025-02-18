package ru.anton.reminder.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.entity.User;
import ru.anton.reminder.repository.ReminderRepository;
import ru.anton.reminder.repository.UserRepository;
import ru.anton.reminder.service.EmailService;
import ru.anton.reminder.service.NotificationService;
import ru.anton.reminder.service.ReminderService;
import ru.anton.reminder.service.TelegramService;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final ReminderService reminderService;
    private final EmailService emailService;
    private final TelegramService telegramService;

    @Override
    @Scheduled(fixedRate = 60000)
    public void checkAndSendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository.findByRemindBefore(now)
                .stream().filter(r -> !r.isSent()).toList();
        if (!reminders.isEmpty()) {
            for (Reminder reminder : reminders) {
                User user = userRepository.findById(reminder.getUser_id()).orElse(null);
                if (user != null && !reminder.isSent()) {
                    reminderService.markAsSent(reminder);
                    emailService.sendEmail(user.getEmail(), reminder.getTitle(), reminder.getDescription());
                    if (user.getTelegramID() != null) {
                        telegramService.sendTelegramMessage(user.getTelegramID(), reminder.getTitle(),
                                reminder.getDescription());
                    }
                }
            }
        }
    }
}
