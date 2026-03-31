package ru.practicum.comments.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "deleted_comments")
public class DeletedComment {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    Long oldCommentId;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    String reason;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    User admin;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    LocalDateTime deletedAt;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    LocalDateTime created;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    String text;
}
