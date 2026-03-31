package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comments.model.DeletedComment;

import java.util.List;
import java.util.Optional;

public interface DeletedCommentsRepository extends JpaRepository<DeletedComment, Long> {

    @Query("SELECT d " +
            "FROM DeletedComment d " +
            "WHERE d.oldCommentId = :id")
    Optional<DeletedComment> getCommentByOldId(@Param("id") Long oldId);

    @Query("SELECT d " +
            "FROM DeletedComment d " +
            "WHERE d.oldCommentId IN :ids")
    List<DeletedComment> findCommentsByIds(@Param("ids") List<Long> ids);

}
