package ru.anton.reminder.dtos.infoRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import ru.anton.reminder.dtos.ReminderDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageDTO {
    Page<ReminderDTO> page;

    boolean isLastPage;
}
