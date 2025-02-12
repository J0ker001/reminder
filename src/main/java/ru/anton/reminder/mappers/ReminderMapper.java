package ru.anton.reminder.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.anton.reminder.dtos.ReminderDTO;
import ru.anton.reminder.entity.Reminder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReminderMapper {
    ReminderDTO toDTO(Reminder reminder);

    Reminder toEntity(ReminderDTO reminderDTO);
}
