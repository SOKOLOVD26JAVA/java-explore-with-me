package ru.practicum.likes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.exceptions.AccessException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.likeDto.LikeRequestDto;
import ru.practicum.likeDto.LikeResponseDto;
import ru.practicum.likes.mapper.LikesMapper;
import ru.practicum.likes.model.Like;
import ru.practicum.likes.repository.LikesRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;

    public LikeResponseDto addLike(LikeRequestDto dto,
                                   Long userId,
                                   Long commentId) {


        User liker = getUserById(userId);
        Comment comment = getCommentById(commentId);

        if (comment.getAuthor().getId() == liker.getId()) {
            throw new AccessException("You cant like your own comment");
        }

        Like like = LikesMapper.mapToLike(dto);
        like.setComment(comment);
        like.setLiker(liker);
        try {
            likesRepository.save(like);
        } catch (DataIntegrityViolationException e) {
            throw new AccessException("You cant rate comment twice");
        }

        return LikesMapper.maptoLikeResponseDto(like);

    }


    public LikeResponseDto updateLike(LikeRequestDto dto,
                                      Long userId,
                                      Long commentId,
                                      Long likeId) {

        User liker = getUserById(userId);
        Comment comment = getCommentById(commentId);
        Like like = getLikeById(likeId);

        if (like.getLiker().getId() != liker.getId()) {
            throw new AccessException("You cant update this like");
        }

        like.setRating(dto.getRating());

        return LikesMapper.maptoLikeResponseDto(likesRepository.save(like));
    }


    public void deleteLike(Long userId,
                           Long commentId,
                           Long likeId) {

        User liker = getUserById(userId);
        Comment comment = getCommentById(commentId);
        Like like = getLikeById(likeId);

        if (like.getLiker().getId() != liker.getId()) {
            throw new AccessException("You cant delete this like");
        }

        likesRepository.delete(like);
    }

    public List<LikeResponseDto> getCommentLikes(Long commentId, int from, int size) {

        Comment comment = getCommentById(commentId);

        List<Like> likes = likesRepository.getCommentLikes(commentId, PageRequest.of(from, size));

        return likes.stream().map(LikesMapper::maptoLikeResponseDto).collect(Collectors.toList());

    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + ", not found."));
    }

    private Comment getCommentById(Long commentId) {
        return commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with ID = " + commentId + ", not found."));
    }

    private Like getLikeById(Long likeId) {
        return likesRepository.findById(likeId)
                .orElseThrow(() -> new NotFoundException("Like with ID = " + likeId + ", not found."));
    }
}
