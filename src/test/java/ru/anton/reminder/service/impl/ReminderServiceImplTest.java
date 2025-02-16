package ru.anton.reminder.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.repository.ReminderRepository;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderServiceImplTest {
    @Mock
    private ReminderRepository reminderRepository;

    @InjectMocks
    private ReminderServiceImpl reminderService;

    private Reminder reminder;

    @BeforeEach
    void setUp() {
        reminder = new Reminder();
        reminder.setId(1L);
        reminder.setTitle("Test Reminder");
        reminder.setDescription("Description");
        reminder.setRemind(LocalDateTime.now());
    }

    @Test
    void testCreateOrUpdateReminder_CreateNew() {
        when(reminderRepository.save(any(Reminder.class))).thenReturn(reminder);
        Reminder savedReminder = reminderService.createOrUpdateReminder(reminder);
        assertNotNull(savedReminder);
        assertEquals("Test Reminder", savedReminder.getTitle());
    }

    @Test
    void testDeleteReminder_Success() {
        when(reminderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reminderRepository).deleteById(1L);

        String result = reminderService.deleteReminder(1L);
        assertEquals("Reminder with Id = 1 deleted", result);
    }

    @Test
    void testDeleteReminder_InvalidId() {
        when(reminderRepository.existsById(2L)).thenReturn(false);
        String result = reminderService.deleteReminder(2L);
        assertEquals("Invalid reminder id, try again", result);
    }

    @Test
    void testSearchReminders() {
        when(reminderRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRemind(
                anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(List.of(reminder));

        List<Reminder> results = reminderService.searchReminders("Test", "Description", LocalDateTime.now());
        assertEquals(1, results.size());
    }

    @Test
    void testSortRemindersByName() {
        when(reminderRepository.findAllByOrderByTitleAsc()).thenReturn(List.of(reminder));
        List<Reminder> sorted = reminderService.sortReminders("name");
        assertEquals(1, sorted.size());
    }

    @Test
    void testGetAllOrByDate_Total() {
        Page<Reminder> page = new PageImpl<>(List.of(reminder));
        when(reminderRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Reminder> result = reminderService.getAllOrByDate("total", LocalDateTime.now(), 0, 10);
        assertEquals(1, result.getContent().size());
    }
}