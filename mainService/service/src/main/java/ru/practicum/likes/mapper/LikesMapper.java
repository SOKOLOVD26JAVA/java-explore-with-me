package ru.practicum.likes.mapper;

import ru.practicum.likeDto.LikeRequestDto;
import ru.practicum.likeDto.LikeResponseDto;
import ru.practicum.likes.model.Like;

public class LikesMapper {

    public static Like mapToLike(LikeRequestDto dto) {
        Like like = new Like();

        like.setRating(dto.getRating());

        return like;
    }

    public static LikeResponseDto maptoLikeResponseDto(Like like) {
        LikeResponseDto dto = new LikeResponseDto();

        dto.setId(like.getId());
        dto.setRating(like.getRating());
        dto.setCommentId(like.getComment().getId());
        dto.setLikerId(like.getLiker().getId());

        return dto;
    }
}
