package ru.anton.reminder.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.repository.ReminderRepository;
import ru.anton.reminder.service.ReminderService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminderRepository;

    @Override
    public Reminder createOrUpdateReminder(Reminder reminder) {
        if (reminder.getId() != null) {
            log.info("Update new Reminder with name: {}", reminder.getTitle());
        } else {
            log.info("Saving new Reminder with name: {}", reminder.getTitle());
        }
        return reminderRepository.save(reminder);
    }

    @Override
    public String deleteReminder(Long id) {
        if (!reminderRepository.existsById(id)) {
            return "Invalid reminder id, try again";
        }
        reminderRepository.deleteById(id);
        return "Reminder with " + "Id = " + id + " deleted";
    }

    @Override
    public List<Reminder> searchReminders(String title, String description, LocalDateTime remind) {
        return reminderRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRemind(
                title, description, remind);
    }

    @Override
    public List<Reminder> sortReminders(String nameOrDateOrTime) {
        return switch (nameOrDateOrTime.toLowerCase()) {
            case "name" -> reminderRepository.findAllByOrderByTitleAsc();
            case "date" -> reminderRepository.findAllByOrderByRemindDateAsc();
            case "time" -> reminderRepository.findAllByOrderByRemindAsc();
            default -> throw new IllegalArgumentException("Invalid sort key: " + nameOrDateOrTime);
        };
    }

    @Override
    public List<Reminder> filterReminders(String dateOrTime, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
        LocalDate localDateStart = dateTimeStart.toLocalDate();
        LocalDate localDateEnd = dateTimeEnd.toLocalDate();

        LocalTime localTimeStart = dateTimeStart.toLocalTime();
        LocalTime localTimeEnd = dateTimeEnd.toLocalTime();

        return switch (dateOrTime.toLowerCase()) {
            case "date" -> reminderRepository.findByDateBetween(localDateStart, localDateEnd);
            case "time" -> reminderRepository.findByTimeBetween(localTimeStart, localTimeEnd);
            default -> throw new IllegalArgumentException("Invalid filter key: " + dateOrTime);
        };
    }

    @Override
    public Page<Reminder> getAllOrByDate(String totalOrCurrent, LocalDateTime dateTime, int page, int size) {
        LocalDate date = dateTime.toLocalDate();
        Pageable pageable = PageRequest.of(page, size);
        return switch (totalOrCurrent.toLowerCase()) {
            case "total" -> reminderRepository.findAll(pageable);
            case "current" -> reminderRepository.findByRemindDay(date, pageable);
            default -> throw new IllegalArgumentException("Invalid key: " + totalOrCurrent);
        };
    }

    @Override
    public void markAsSent(Reminder reminder) {
        reminder.setSent(true);
        reminderRepository.save(reminder);
    }
}
