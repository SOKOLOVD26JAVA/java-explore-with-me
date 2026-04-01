package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.commentDto.CommentRequestDto;
import ru.practicum.commentDto.CommentResponseDto;
import ru.practicum.comments.mapper.CommentsMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentWithRating;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.eventsDto.State;
import ru.practicum.exceptions.AccessException;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.BadTimeException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;


    public CommentResponseDto createComment(CommentRequestDto dto, Long eventId, Long userId) {

        Event event = getEventById(eventId);
        User author = getUserById(userId);
        if (event.getState() != State.PUBLISHED) {
            throw new AccessException("Event with ID = " + eventId + ", not found.");
        }

        Comment comment = CommentsMapper.mapToComment(dto);
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(author);
        comment.setEvent(event);

        return CommentsMapper.mapToCommentResponseDto(commentsRepository.save(comment));
    }

    public CommentResponseDto updateComment(CommentRequestDto dto, Long eventId, Long userId, Long commentId) {
        Comment comment = getCommentById(commentId);
        Event event = getEventById(eventId);
        User author = getUserById(userId);

        if (!Objects.equals(comment.getAuthor().getId(), author.getId())) {
            throw new AccessException("You cant update this comment");
        }

        comment.setText(dto.getText());

        return CommentsMapper.mapToCommentResponseDto(commentsRepository.save(comment));
    }

    public void deleteComment(Long eventId, Long userId, Long commentId) {
        Comment comment = getCommentById(commentId);
        Event event = getEventById(eventId);
        User author = getUserById(userId);

        if (!Objects.equals(comment.getAuthor().getId(), author.getId())) {
            throw new AccessException("You cant delete this comment");
        }

        commentsRepository.delete(comment);
    }

    public List<CommentResponseDto> getUserComments(Long userId, Long eventId, int from, int size) {
        List<CommentWithRating> comments = commentsRepository.findAllByUserId(eventId, userId, PageRequest.of(from, size));
        return comments.stream().map(row -> {
            CommentResponseDto dto = CommentsMapper.mapToCommentResponseDto(row.getComment());
            Double rating = row.getRating() != null ? row.getRating() : 0.0;
            dto.setRating(rating);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<CommentResponseDto> getAllCommentsEvent(Long eventId, String sort, int from, int size) {
        Event event = getEventById(eventId);

        List<CommentWithRating> comments = commentsRepository.findAllByEventId(eventId, PageRequest.of(from, size));
        List<CommentResponseDto> allComments = comments.stream().map(row -> {
            CommentResponseDto dto = CommentsMapper.mapToCommentResponseDto(row.getComment());
            Double rating = row.getRating() != null ? row.getRating() : 0.0;
            dto.setRating(rating);
            return dto;
        }).toList();

        if (sort == null) {
            return allComments;
        }

        return switch (sort) {
            case "OLD" ->
                    allComments.stream().sorted(Comparator.comparing(CommentResponseDto::getCreated).reversed()).collect(Collectors.toList());
            case "NEW" ->
                    allComments.stream().sorted(Comparator.comparing(CommentResponseDto::getCreated)).collect(Collectors.toList());
            case "RATE" ->
                    allComments.stream().sorted(Comparator.comparing(CommentResponseDto::getRating).reversed()).collect(Collectors.toList());
            default -> throw new BadRequestException("Incorrectly made request.");
        };

    }

    public List<CommentResponseDto> getCommentEventSearch(Long eventId, String text, int from, int size) {
        Event event = getEventById(eventId);

        if (text == null) {
            throw new BadTimeException("Incorrectly made request.");
        }

        List<CommentWithRating> comments = commentsRepository.findAllByEventIdAndText(eventId, text, PageRequest.of(from, size));

        return comments.stream().map(row -> {
            CommentResponseDto dto = CommentsMapper.mapToCommentResponseDto(row.getComment());
            Double rating = row.getRating() != null ? row.getRating() : 0.0;
            dto.setRating(rating);
            return dto;
        }).toList();

    }

    public List<CommentResponseDto> getUserCommentsAdmin(Long userId,
                                                         int from,
                                                         int size) {
        User user = getUserById(userId);

        List<Comment> comments = commentsRepository.findAllUserComments(userId, PageRequest.of(from, size));

        return comments.stream().map(CommentsMapper::mapToCommentResponseDto).collect(Collectors.toList());
    }

    public void deleteCommentByAdmin(Long adminId,
                                     Long commentId,
                                     String reason) {
        if (reason == null) {
            throw new AccessException("You cant delete comment with out reason. Please, specify the reason.");
        }
        Comment comment = getCommentById(commentId);
        User admin = getUserById(adminId);

        comment.setDeletedAt(LocalDateTime.now());
        comment.setDeletedBy(admin);
        comment.setReason(reason);

        commentsRepository.save(comment);
    }

    public List<CommentResponseDto> getDeletedComments(int from,
                                                       int size) {
        List<Comment> deletedComments = commentsRepository.getDeletedComments(PageRequest.of(from, size));

        return deletedComments.stream().map(CommentsMapper::mapToCommentResponseDto)
                .sorted(Comparator.comparing(CommentResponseDto::getDeletedAt).reversed()).collect(Collectors.toList());
    }

    public CommentResponseDto getDeletedCommentById(Long oldCommentId) {
        Comment comment = commentsRepository.getDeletedCommentById(oldCommentId).orElseThrow(() -> new NotFoundException("Old comment with ID = " + oldCommentId + ", not found."));
        return CommentsMapper.mapToCommentResponseDto(comment);
    }

    public List<CommentResponseDto> returnDeletedComments(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BadTimeException("Choose comments for return");
        }

        List<Comment> deletedComments = commentsRepository.findDeletedCommentsByIds(ids);
        if (deletedComments.isEmpty()) {
            throw new BadRequestException("Deleted comments not found.");
        }

        for (Comment comment : deletedComments) {
            comment.setDeletedBy(null);
            comment.setDeletedAt(null);
            comment.setReason(null);
        }

        commentsRepository.saveAll(deletedComments);

        return deletedComments.stream().map(CommentsMapper::mapToCommentResponseDto).collect(Collectors.toList());
    }


    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + ", not found."));
    }

    private Event getEventById(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID = " + eventId + ", not found."));
    }

    private Comment getCommentById(Long commentId) {
        return commentsRepository.getCommentById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with ID = " + commentId + ", not found."));
    }

}
