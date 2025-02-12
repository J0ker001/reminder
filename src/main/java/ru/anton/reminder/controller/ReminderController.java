package ru.anton.reminder.controller;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anton.reminder.dtos.infoRequest.*;
import ru.anton.reminder.dtos.ReminderDTO;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.service.ReminderService;


import java.util.List;

@RestController
@RequestMapping("/domain/api/v1")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    public String welcome() {
        return "Welcome to Reminder";
    }

    @PostMapping("/reminder/create")
    public ResponseEntity<ReminderDTO> createReminder(@Valid @RequestBody ReminderDTO reminderDTO) {
        return new ResponseEntity<>(reminderService.createOrUpdateReminder(reminderDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/reminder/delete")
    public String deleteReminder(@RequestBody InfoRequestDeletedDTO info) {
        return reminderService.deleteReminder(info);
    }

    @GetMapping("/search")
    public List<Reminder> searchReminders(@RequestBody InfoRequestSearchDTO info) {
        return reminderService.searchReminders(info);
    }

    @GetMapping("/sort")
    public List<Reminder> sortReminders(@RequestBody InfoRequestSortDTO info) {
        return reminderService.sortReminders(info);
    }

    @GetMapping("/filter")
    public List<Reminder> filterReminders(@RequestBody InfoRequestFilterDTO info) {
        return reminderService.filterReminders(info);
    }

    @GetMapping("/list")
    public Page<Reminder> getAllOrByDate(@RequestBody InfoRequestByDateDTO info,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "2") int size) {
        return reminderService.getAllOrByDate(info, page, size);
    }
}
