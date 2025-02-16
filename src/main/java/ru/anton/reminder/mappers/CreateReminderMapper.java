package ru.anton.reminder.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.anton.reminder.dtos.CreateReminderDTO;
import ru.anton.reminder.entity.Reminder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreateReminderMapper {
    Reminder toEntity(CreateReminderDTO createReminderDTO);
}
