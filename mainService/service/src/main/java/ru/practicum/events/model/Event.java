package ru.practicum.events.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.categories.model.Category;
import ru.practicum.eventsDto.State;
import ru.practicum.locationModel.Location;
import ru.practicum.requests.model.Request;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "events")
public class Event {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Embedded
    private Location location;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private Boolean paid;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private Integer participantLimit;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private LocalDateTime publishedOn;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private LocalDateTime createdOn;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private Boolean requestModeration;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    @Enumerated(EnumType.STRING)
    private State state;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private String title;

    @OneToMany(mappedBy = "event")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Request> requests = new ArrayList<>();
}
