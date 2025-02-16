package ru.anton.reminder.dtos.infoRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InfoRequestSendTelegramDTO {
    private String chatId;
    private String title;
    private String description;
}
