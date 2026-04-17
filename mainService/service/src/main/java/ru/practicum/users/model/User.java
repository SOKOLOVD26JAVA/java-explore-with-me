package ru.practicum.users.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.model.Event;
import ru.practicum.requests.model.Request;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private String email;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private Boolean isAdmin;

    @OneToMany(mappedBy = "initiator")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Event> events;

    @OneToMany(mappedBy = "requester")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

}
