package ru.practicum.likes.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.comments.model.Comment;
import ru.practicum.users.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "likes")
public class Like {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Include
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "liker_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User liker;
}
