package ru.practicum.likes.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.likes.model.Like;

import java.util.List;

public interface LikesRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l " +
            "FROM Like l " +
            "WHERE l.comment.id = :commentId")
    List<Like> getCommentLikes(@Param("commentId") Long commentId,
                               Pageable pageable);

}
