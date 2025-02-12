package ru.anton.reminder.service;


import ru.anton.reminder.dtos.infoRequest.InfoRequestAddTelegramIdDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestDeletedDTO;
import ru.anton.reminder.entity.User;

public interface UserService {

    User findOrCreateUser(String email, String name, String providerId);

    String delete(InfoRequestDeletedDTO info);

    String addTelegramID(InfoRequestAddTelegramIdDTO info);


}
