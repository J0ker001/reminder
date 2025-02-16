package ru.anton.reminder.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anton.reminder.entity.User;
import ru.anton.reminder.repository.UserRepository;
import ru.anton.reminder.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findOrCreateUser(String email, String name, String providerId) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        return existingUser.orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .providerId(providerId)
                    .build();
            log.info("Saving new user with name: {}", email);
            return userRepository.save(newUser);
        });
    }

    @Override
    public String delete(Long id) {
        if (!userRepository.existsById(id)) {
            return "Invalid user id, try again";
        }
        userRepository.deleteById(id);
        return "User with " + "Id = " + id + " deleted";
    }

    @Override
    public String addTelegramID(Long id, String telegramId) {
        if (!isValidTelegramId(telegramId)) {
            log.info("Invalid telegram id, try again");
            return "Invalid telegram id, try again";
        }
        User user = userRepository.findById(id).get();
        user.setTelegramID(telegramId);
        userRepository.save(user);
        log.info("{} TelegramID added", telegramId);
        return telegramId + " TelegramID added";
    }

    private boolean isValidTelegramId(String telegramID) {
        if (telegramID == null || telegramID.trim().isEmpty()) return false;

        try {
            Long.parseLong(telegramID);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
