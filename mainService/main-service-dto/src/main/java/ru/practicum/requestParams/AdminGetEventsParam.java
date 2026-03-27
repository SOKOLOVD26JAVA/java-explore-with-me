package ru.practicum.requestParams;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.eventsDto.State;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminGetEventsParam {

    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
    private int from = 0;
    private int size = 10;
}
