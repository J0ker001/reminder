package ru.anton.reminder.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anton.reminder.dtos.infoRequest.InfoRequestAddTelegramIdDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestDeletedDTO;
import ru.anton.reminder.entity.User;
import ru.anton.reminder.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser;
    private User newUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .providerId("google")
                .build();

        newUser = User.builder()
                .email("new@example.com")
                .name("New User")
                .providerId("google")
                .build();
    }

    @Test
    void testFindOrCreateUser_WhenUserExists_ShouldReturnExistingUser() {
        // Arrange
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(existingUser));

        // Act
        User result = userService.findOrCreateUser("test@example.com", "Test User", "google");

        // Assert
        assertEquals(existingUser, result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class)); // Ensure no save is done
    }

    @Test
    void testFindOrCreateUser_WhenUserDoesNotExist_ShouldReturnNewUser() {
        // Arrange
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userService.findOrCreateUser("new@example.com", "New User", "google");

        // Assert
        assertEquals(newUser, result);
        verify(userRepository, times(1)).findByEmail("new@example.com");
        verify(userRepository, times(1)).save(any(User.class)); // Ensure save is called
    }

    @Test
    void testDelete_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        InfoRequestDeletedDTO deleteInfo = new InfoRequestDeletedDTO();
        deleteInfo.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        String result = userService.delete(deleteInfo);

        // Assert
        assertEquals("User with Id = 1 deleted", result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_WhenUserDoesNotExist_ShouldReturnError() {
        // Arrange
        InfoRequestDeletedDTO deleteInfo = new InfoRequestDeletedDTO();
        deleteInfo.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act
        String result = userService.delete(deleteInfo);

        // Assert
        assertEquals("Invalid user id, try again", result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAddTelegramID_ShouldAddTelegramId() {
        // Arrange
        InfoRequestAddTelegramIdDTO addTelegramIdInfo = new InfoRequestAddTelegramIdDTO();
        addTelegramIdInfo.setId(1L);
        addTelegramIdInfo.setTelegramId("12345");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        //Act
        String result = userService.addTelegramID(addTelegramIdInfo);

        //Assert
        assertEquals("12345 TelegramID added", result);
        assertEquals("12345", existingUser.getTelegramID());  // Ensure Telegram ID was set
        verify(userRepository, times(1)).save(existingUser);  // Ensure user was saved
    }
}