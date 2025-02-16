package ru.anton.reminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anton.reminder.dtos.infoRequest.InfoRequestAddTelegramIdDTO;
import ru.anton.reminder.dtos.infoRequest.InfoRequestDeletedDTO;
import ru.anton.reminder.service.UserService;

@RestController
@RequestMapping("/domain/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/delete")
    public String delete(@RequestBody InfoRequestDeletedDTO info) {
        return userService.delete(info.getId());
    }

    @PostMapping("/addID")
    public String addID(@RequestBody InfoRequestAddTelegramIdDTO info) {
        return userService.addTelegramID(info.getId(), info.getTelegramId());
    }
}
