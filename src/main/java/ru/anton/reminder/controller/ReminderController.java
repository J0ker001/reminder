package ru.anton.reminder.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anton.reminder.dtos.CreateReminderDTO;
import ru.anton.reminder.dtos.ReminderDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestDeletedDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestFilterDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestGetTotalOrCurrent;
import ru.anton.reminder.dtos.infoRequest.InfoRequestSearchDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestSortDTO;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.mappers.CreateReminderMapper;
import ru.anton.reminder.mappers.ReminderMapper;
import ru.anton.reminder.service.ReminderService;

@RestController
@RequestMapping("/domain/api/v1")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;
    private final ReminderMapper reminderMapper;
    private final CreateReminderMapper createReminderMapper;

    @GetMapping
    public String welcome() {
        return "Welcome to Reminder";
    }

    @PostMapping("/reminder/create")
    public ResponseEntity<ReminderDTO> createReminder(@Valid @RequestBody CreateReminderDTO createReminderDTO) {
        Reminder reminder = createReminderMapper.toEntity(createReminderDTO);
        ReminderDTO reminderDTO = reminderMapper.toDTO(reminderService.createOrUpdateReminder(reminder));
        return new ResponseEntity<>(reminderDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/reminder/delete")
    public String deleteReminder(@RequestBody InfoRequestDeletedDTO info) {
        return reminderService.deleteReminder(info.getId());
    }

    @GetMapping("/search")
    public List<ReminderDTO> searchReminders(@RequestBody InfoRequestSearchDTO info) {
        return reminderMapper.toListDTO(reminderService.searchReminders(info.getTitle(),
                info.getDescription(), info.getRemind()));
    }

    @GetMapping("/sort")
    public List<ReminderDTO> sortReminders(@RequestBody InfoRequestSortDTO info) {
        return reminderMapper.toListDTO(reminderService.sortReminders(info.getNameOrDateOrTime()));
    }

    @GetMapping("/filter")
    public List<ReminderDTO> filterReminders(@RequestBody InfoRequestFilterDTO info) {
        return reminderMapper.toListDTO(reminderService.filterReminders(info.getDateOrTime(),
                info.getDateTimeStart(), info.getDateTimeEnd()));
    }

    @GetMapping("/list")
    public Page<Reminder> getAllOrByDate(@RequestBody InfoRequestGetTotalOrCurrent info) {
        return reminderService.getAllOrByDate(info.getTotalOrCurrent(), info.getDateTime(),
                info.getPage(), info.getSize());
    }
}
