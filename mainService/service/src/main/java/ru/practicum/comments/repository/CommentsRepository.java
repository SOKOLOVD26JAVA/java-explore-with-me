package ru.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentWithRating;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c as comment, AVG(l.rating) as rating " +
            "FROM Comment c " +
            "LEFT JOIN Like l ON l.comment.id = c.id " +
            "WHERE c.event.id = :eventId AND c.author.id = :userId " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY c")
    List<CommentWithRating> findAllByUserId(@Param("eventId") Long eventId,
                                            @Param("userId") Long userId,
                                            Pageable pageable);

    @Query("SELECT c as comment, AVG(l.rating) as rating " +
            "FROM Comment c " +
            "LEFT JOIN Like l ON l.comment.id = c.id " +
            "WHERE c.event.id = :eventId " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY c")
    List<CommentWithRating> findAllByEventId(@Param("eventId") Long eventId,
                                             Pageable pageable);

    @Query("SELECT c as comment, AVG(l.rating) as rating " +
            "FROM Comment c " +
            "LEFT JOIN Like l ON l.comment.id = c.id " +
            "WHERE c.event.id = :eventId AND LOWER(c.text) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY c")
    List<CommentWithRating> findAllByEventIdAndText(@Param("eventId") Long eventId,
                                                    @Param("text") String text,
                                                    Pageable pageable);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.author.id = :userId AND c.deletedAt IS NULL")
    List<Comment> findAllUserComments(@Param("userId") Long userId,
                                      Pageable pageable);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.deletedAt IS NOT NULL")
    List<Comment> getDeletedComments(Pageable pageable);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.deletedAt IS NOT NULL AND c.id = :id")
    Optional<Comment> getDeletedCommentById(@Param("id") Long id);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.deletedAt IS NULL AND c.id = :id")
    Optional<Comment> getCommentById(@Param("id") Long id);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.deletedAt IS NOT NULL AND c.id IN :ids")
    List<Comment> findDeletedCommentsByIds(@Param("ids") List<Long> ids);
}
