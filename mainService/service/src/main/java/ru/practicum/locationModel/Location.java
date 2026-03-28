package ru.practicum.locationModel;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Location {
    private Double lat;
    private Double lon;
}
