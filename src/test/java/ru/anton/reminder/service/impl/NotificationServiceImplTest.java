package ru.anton.reminder.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anton.reminder.entity.Reminder;
import ru.anton.reminder.entity.User;
import ru.anton.reminder.repository.ReminderRepository;
import ru.anton.reminder.repository.UserRepository;
import ru.anton.reminder.service.EmailService;
import ru.anton.reminder.service.ReminderService;
import ru.anton.reminder.service.TelegramService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReminderService reminderService;

    @Mock
    private EmailService emailService;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Reminder reminder;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создаем тестового пользователя
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .telegramID("telegramId123")
                .build();

        // Создаем напоминание
        reminder = Reminder.builder()
                .id(1L)
                .user_id(1L)
                .title("Test Reminder")
                .description("This is a test reminder")
                .remind(LocalDateTime.now().minusMinutes(1))  // Напоминание должно быть выполнено
                .isSent(false)  // Напоминание еще не отправлено
                .build();
    }

    @Test
    void testCheckAndSendNotifications_WhenReminderIsValid_ShouldSendNotifications() {
        // Arrange
        when(reminderRepository.findByRemindBefore(any(LocalDateTime.class))).thenReturn(Arrays.asList(reminder));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));


        // Act
        notificationService.checkAndSendNotifications();

        // Assert
        verify(reminderRepository, times(1)).findByRemindBefore(any(LocalDateTime.class)); // Проверка вызова репозитория для получения напоминаний
        verify(userRepository, times(1)).findById(1L); // Проверка вызова репозитория для получения пользователя
        verify(reminderService, times(1)).markAsSent(reminder); // Проверка, что напоминание помечено как отправленное
        verify(emailService, times(1)).sendEmail(user.getEmail(), reminder.getTitle(), reminder.getDescription()); // Проверка отправки email
        verify(telegramService, times(1)).sendTelegramMessage(user.getTelegramID(), reminder.getTitle(), reminder.getDescription()); // Проверка отправки telegram сообщения
    }

    @Test
    void testCheckAndSendNotifications_WhenReminderIsAlreadySent_ShouldNotSendNotifications() {
        // Arrange
        reminder.setSent(true);  // Устанавливаем, что напоминание уже отправлено
        when(reminderRepository.findByRemindBefore(any(LocalDateTime.class))).thenReturn(Arrays.asList(reminder));

        // Act
        notificationService.checkAndSendNotifications();

        // Assert
        verify(reminderRepository, times(1)).findByRemindBefore(any(LocalDateTime.class)); // Проверка вызова репозитория для получения напоминаний
        verify(reminderService, never()).markAsSent(any(Reminder.class)); // Убедимся, что напоминание не было помечено как отправленное
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString()); // Убедимся, что email не был отправлен
        verify(telegramService, never()).sendTelegramMessage(anyString(), anyString(), anyString()); // Убедимся, что telegram сообщение не было отправлено
    }

    @Test
    void testCheckAndSendNotifications_WhenUserNotFound_ShouldNotSendNotifications() {
        // Arrange
        when(reminderRepository.findByRemindBefore(any(LocalDateTime.class))).thenReturn(Arrays.asList(reminder));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty()); // Пользователь не найден

        // Act
        notificationService.checkAndSendNotifications();

        // Assert
        verify(userRepository, times(1)).findById(1L); // Проверка вызова репозитория для получения пользователя
        verify(reminderService, never()).markAsSent(any(Reminder.class)); // Напоминание не должно быть помечено как отправленное
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString()); // Email не отправлен
        verify(telegramService, never()).sendTelegramMessage(anyString(), anyString(), anyString()); // Telegram сообщение не отправлено
    }

    @Test
    void testCheckAndSendNotifications_WhenReminderListIsEmpty_ShouldNotSendNotifications() {
        // Arrange
        when(reminderRepository.findByRemindBefore(any(LocalDateTime.class))).thenReturn(Arrays.asList()); // Пустой список напоминаний

        // Act
        notificationService.checkAndSendNotifications();

        // Assert
        verify(reminderRepository, times(1)).findByRemindBefore(any(LocalDateTime.class)); // Проверка вызова репозитория
        verify(userRepository, never()).findById(anyLong()); // Не должно быть вызова репозитория для получения пользователя
        verify(reminderService, never()).markAsSent(any(Reminder.class)); // Напоминание не должно быть помечено как отправленное
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString()); // Email не отправлен
        verify(telegramService, never()).sendTelegramMessage(anyString(), anyString(), anyString()); // Telegram не отправлен
    }
}