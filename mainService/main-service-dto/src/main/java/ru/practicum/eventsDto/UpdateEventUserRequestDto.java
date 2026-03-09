package ru.practicum.eventsDto;

import lombok.Data;



@Data
public class UpdateEventUserRequestDto  extends BaseUpdateEventRequest{
    private UserStateAction stateAction;
}
