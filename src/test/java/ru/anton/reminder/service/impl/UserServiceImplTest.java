package ru.anton.reminder.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anton.reminder.entity.User;
import ru.anton.reminder.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .providerId("provider123")
                .telegramID("123456789")
                .build();
    }

    @Test
    void findOrCreateUser_UserExists_ReturnsExistingUser() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        User result = userService.findOrCreateUser(testUser.getEmail(), testUser.getName(), testUser.getProviderId());

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findOrCreateUser_UserNotExists_CreatesNewUser() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.findOrCreateUser(testUser.getEmail(), testUser.getName(), testUser.getProviderId());

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void delete_UserExists_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        String result = userService.delete(1L);

        assertEquals("User with Id = 1 deleted", result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_UserNotExists_Failure() {
        when(userRepository.existsById(1L)).thenReturn(false);

        String result = userService.delete(1L);

        assertEquals("Invalid user id, try again", result);
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    void addTelegramID_ValidTelegramId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String result = userService.addTelegramID(1L, "987654321");

        assertEquals("987654321 TelegramID added", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addTelegramID_InvalidTelegramId_Failure() {
        String result = userService.addTelegramID(1L, "invalid_id");

        assertEquals("Invalid telegram id, try again", result);
        verify(userRepository, never()).save(any(User.class));
    }
}