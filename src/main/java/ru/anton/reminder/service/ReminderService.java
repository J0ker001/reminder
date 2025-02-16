package ru.anton.reminder.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import ru.anton.reminder.entity.Reminder;

public interface ReminderService {
    Reminder createOrUpdateReminder(Reminder reminder);

    String deleteReminder(Long id);

    List<Reminder> searchReminders(String title, String description, LocalDateTime remind);

    List<Reminder> sortReminders(String nameOrDateOrTime);

    List<Reminder> filterReminders(String dateOrTime, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);

    Page<Reminder> getAllOrByDate(String totalOrCurrent, LocalDateTime dateTime, int page, int size);

    void markAsSent(Reminder reminder);
}
