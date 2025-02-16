package ru.anton.reminder.dtos.infoRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class infoRequestSendEmailDTO {
    private String to;
    private String subject;
    private String text;
}
