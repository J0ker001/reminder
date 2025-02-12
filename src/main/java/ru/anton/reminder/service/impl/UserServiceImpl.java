package ru.anton.reminder.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anton.reminder.dtos.infoRequest.InfoRequestAddTelegramIdDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestDeletedDTO;
import ru.anton.reminder.entity.User;
import ru.anton.reminder.repository.UserRepository;
import ru.anton.reminder.service.UserService;

import java.util.Optional;

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
    public String delete(InfoRequestDeletedDTO info) {
        if (!userRepository.existsById(info.getId())) {
            return "Invalid user id, try again";
        }
        userRepository.deleteById(info.getId());
        return "User with " + "Id = " + info.getId() + " deleted";
    }

    @Override
    public String addTelegramID(InfoRequestAddTelegramIdDTO info) {
        if (!isValidTelegramId(info.getTelegramId())) {
            log.info("Invalid telegram id, try again");
            return "Invalid telegram id, try again";
        }
        User user = userRepository.findById(info.getId()).get();
        user.setTelegramID(info.getTelegramId());
        userRepository.save(user);
        log.info("{} TelegramID added", info.getTelegramId());
        return info.getTelegramId() + " TelegramID added";
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
