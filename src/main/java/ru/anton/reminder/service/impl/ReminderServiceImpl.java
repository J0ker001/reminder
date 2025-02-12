package ru.anton.reminder.service.impl;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.anton.reminder.dtos.infoRequest.*;
import ru.anton.reminder.dtos.ReminderDTO;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.mappers.ReminderMapper;
import ru.anton.reminder.repository.ReminderRepository;
import ru.anton.reminder.service.ReminderService;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderServiceImpl implements ReminderService {
    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;

    @Override
    public ReminderDTO createOrUpdateReminder(ReminderDTO reminderDTO) {
        Reminder reminder = reminderMapper.toEntity(reminderDTO);
        if (reminderDTO.getId() != null) {
            log.info("Update new Reminder with name: {}", reminder.getTitle());
        } else {
            log.info("Saving new Reminder with name: {}", reminder.getTitle());
        }
        return reminderMapper.toDTO(reminderRepository.save(reminder));
    }

    @Override
    public String deleteReminder(InfoRequestDeletedDTO info) {
        if (!reminderRepository.existsById(info.getId())) {
            return "Invalid reminder id, try again";
        }
        reminderRepository.deleteById(info.getId());
        return "Reminder with " + "Id = " + info.getId() + " deleted";
    }

    @Override
    public List<Reminder> searchReminders(InfoRequestSearchDTO info) {
        return reminderRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRemind(
                info.getTitle(), info.getDescription(), info.getRemind());
    }

    @Override
    public List<Reminder> sortReminders(InfoRequestSortDTO info) {
        return switch (info.getNameOrDateOrTime().toLowerCase()) {
            case "name" -> reminderRepository.findAllByOrderByTitleAsc();
            case "date" -> reminderRepository.findAllByOrderByRemindDateAsc();
            case "time" -> reminderRepository.findAllByOrderByRemindAsc();
            default -> throw new IllegalArgumentException("Invalid sort key: " + info.getNameOrDateOrTime());
        };
    }

    @Override
    public List<Reminder> filterReminders(InfoRequestFilterDTO info) {

        LocalDate localDateStart = info.getDateTimeStart().toLocalDate();
        LocalDate localDateEnd = info.getDateTimeEnd().toLocalDate();

        LocalTime localTimeStart = info.getDateTimeStart().toLocalTime();
        LocalTime localTimeEnd = info.getDateTimeEnd().toLocalTime();

        return switch (info.getDateOrTime().toLowerCase()) {
            case "date" -> reminderRepository.findByDateBetween(localDateStart, localDateEnd);
            case "time" -> reminderRepository.findByTimeBetween(localTimeStart, localTimeEnd);
            default -> throw new IllegalArgumentException("Invalid filter key: " + info.getDateOrTime());
        };
    }

    @Override
    public Page<Reminder> getAllOrByDate(InfoRequestByDateDTO info, int page, int size) {
        LocalDate date = info.getDateTime().toLocalDate();
        Pageable pageable = PageRequest.of(page, size);
        return switch (info.getTotalOrCurrent().toLowerCase()) {
            case "total" -> reminderRepository.findAll(pageable);
            case "current" -> reminderRepository.findByRemindDay(date, pageable);
            default -> throw new IllegalArgumentException("Invalid key: " + info.getTotalOrCurrent());
        };
    }

    @Override
    public void markAsSent(Reminder reminder) {
        reminder.setSent(true);
        reminderRepository.save(reminder);
    }
}
