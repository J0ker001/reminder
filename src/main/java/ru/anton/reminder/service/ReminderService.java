package ru.anton.reminder.service;

import org.springframework.data.domain.Page;
import ru.anton.reminder.dtos.infoRequest.*;
import ru.anton.reminder.dtos.ReminderDTO;
import ru.anton.reminder.entity.Reminder;

import java.util.List;

public interface ReminderService {

    ReminderDTO createOrUpdateReminder(ReminderDTO reminderDTO);

    String deleteReminder(InfoRequestDeletedDTO info);

    List<Reminder> searchReminders(InfoRequestSearchDTO info);

    List<Reminder> sortReminders(InfoRequestSortDTO info);

    List<Reminder> filterReminders(InfoRequestFilterDTO info);

    Page<Reminder> getAllOrByDate(InfoRequestByDateDTO info, int page, int size);

    void markAsSent(Reminder reminder);
}
