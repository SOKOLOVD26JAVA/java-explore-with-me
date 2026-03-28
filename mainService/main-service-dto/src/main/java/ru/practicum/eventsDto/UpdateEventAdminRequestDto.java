package ru.practicum.eventsDto;


import lombok.Data;

@Data
public class UpdateEventAdminRequestDto extends BaseUpdateEventRequest {


    private AdminStateAction stateAction;
}
