package ru.practicum.comments.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.likes.model.Like;
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
@Table(name = "comments")
public class Comment {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private String text;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Event event;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private LocalDateTime created;

    @OneToMany(mappedBy = "comment")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Like> likes = new ArrayList<>();

}
