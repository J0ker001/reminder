package ru.anton.reminder.service;

import ru.anton.reminder.entity.User;

public interface UserService {
    User findOrCreateUser(String email, String name, String providerId);

    String delete(Long id);

    String addTelegramID(Long id, String telegramId);
}
