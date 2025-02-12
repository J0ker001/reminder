package ru.anton.reminder.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.anton.reminder.dtos.ReminderDTO;
import ru.anton.reminder.dtos.infoRequest.*;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.mappers.ReminderMapper;
import ru.anton.reminder.repository.ReminderRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ReminderServiceImplTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private ReminderMapper reminderMapper;

    @Mock
    private Logger log;

    private ReminderServiceImpl reminderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reminderService = new ReminderServiceImpl(reminderRepository, reminderMapper);
    }

    @Test
    void testCreateOrUpdateReminder() {
        // Создаем DTO для нового напоминания
        ReminderDTO reminderDTO = new ReminderDTO();
        reminderDTO.setTitle("New Reminder");
        reminderDTO.setDescription("Test Description");

        Reminder reminder = new Reminder();
        reminder.setTitle(reminderDTO.getTitle());
        reminder.setDescription(reminderDTO.getDescription());

        // Мокаем маппер и репозиторий
        when(reminderMapper.toEntity(reminderDTO)).thenReturn(reminder);
        when(reminderRepository.save(reminder)).thenReturn(reminder);
        when(reminderMapper.toDTO(reminder)).thenReturn(reminderDTO);

        // Вызов метода
        ReminderDTO result = reminderService.createOrUpdateReminder(reminderDTO);

        // Проверки
        verify(reminderRepository, times(1)).save(reminder);
        assertNotNull(result);
        assertEquals("New Reminder", result.getTitle());
        assertEquals("Test Description", result.getDescription());
    }

    @Test
    void testDeleteReminder() {
        InfoRequestDeletedDTO info = new InfoRequestDeletedDTO();
        info.setId(1L);

        // Мокаем существование записи
        when(reminderRepository.existsById(info.getId())).thenReturn(true);

        // Вызов метода
        String result = reminderService.deleteReminder(info);

        // Проверки
        verify(reminderRepository, times(1)).deleteById(info.getId());
        assertEquals("Reminder with Id = 1 deleted", result);
    }

    @Test
    void testDeleteReminderNotFound() {
        InfoRequestDeletedDTO info = new InfoRequestDeletedDTO();
        info.setId(1L);

        // Мокаем отсутствие записи
        when(reminderRepository.existsById(info.getId())).thenReturn(false);

        // Вызов метода
        String result = reminderService.deleteReminder(info);

        // Проверки
        verify(reminderRepository, never()).deleteById(info.getId());
        assertEquals("Invalid reminder id, try again", result);
    }

    @Test
    void testSearchReminders() {
        InfoRequestSearchDTO searchDTO = new InfoRequestSearchDTO();
        searchDTO.setTitle("Reminder");

        List<Reminder> reminders = List.of(new Reminder(), new Reminder());

        // Мокаем репозиторий
        when(reminderRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRemind(
                searchDTO.getTitle(), searchDTO.getDescription(), searchDTO.getRemind()))
                .thenReturn(reminders);

        // Вызов метода
        List<Reminder> result = reminderService.searchReminders(searchDTO);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSortReminders() {
        InfoRequestSortDTO sortDTO = new InfoRequestSortDTO();
        sortDTO.setNameOrDateOrTime("name");

        List<Reminder> reminders = List.of(new Reminder(), new Reminder());

        // Мокаем репозиторий
        when(reminderRepository.findAllByOrderByTitleAsc()).thenReturn(reminders);

        // Вызов метода
        List<Reminder> result = reminderService.sortReminders(sortDTO);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFilterReminders() {
        InfoRequestFilterDTO filterDTO = new InfoRequestFilterDTO();
        filterDTO.setDateOrTime("date");
        filterDTO.setDateTimeStart(LocalDateTime.of(2023, 1, 1, 0, 0));
        filterDTO.setDateTimeEnd(LocalDateTime.of(2023, 12, 31, 23, 59));

        List<Reminder> reminders = List.of(new Reminder(), new Reminder());

        // Мокаем репозиторий
        when(reminderRepository.findByDateBetween(any(), any())).thenReturn(reminders);

        // Вызов метода
        List<Reminder> result = reminderService.filterReminders(filterDTO);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllOrByDate() {
        InfoRequestByDateDTO dateDTO = new InfoRequestByDateDTO();
        dateDTO.setTotalOrCurrent("total");
        dateDTO.setDateTime(LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Reminder> remindersPage = mock(Page.class);

        // Мокаем репозиторий
        when(reminderRepository.findAll(pageable)).thenReturn(remindersPage);

        // Вызов метода
        Page<Reminder> result = reminderService.getAllOrByDate(dateDTO, 0, 10);

        // Проверки
        assertNotNull(result);
    }

    @Test
    void testMarkAsSent() {
        Reminder reminder = new Reminder();
        reminder.setSent(false);

        // Мокаем репозиторий
        when(reminderRepository.save(reminder)).thenReturn(reminder);

        // Вызов метода
        reminderService.markAsSent(reminder);

        // Проверки
        assertTrue(reminder.isSent());
        verify(reminderRepository, times(1)).save(reminder);
    }
}