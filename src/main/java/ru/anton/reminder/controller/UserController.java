package ru.anton.reminder.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
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
        return userService.delete(info);
    }

    @PostMapping("/addID")
    public String addID(@RequestBody InfoRequestAddTelegramIdDTO info) {
        return userService.addTelegramID(info);
    }
}
