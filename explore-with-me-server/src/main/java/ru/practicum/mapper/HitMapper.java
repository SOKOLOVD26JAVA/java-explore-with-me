package ru.practicum.mapper;

import ru.practicum.hitDto.HitDto;
import ru.practicum.model.Hit;

public class HitMapper {

    public static HitDto mapToHitDto(Hit hit) {
        HitDto hitDto = new HitDto();

        hitDto.setApp(hit.getApp());
        hitDto.setIp(hit.getIp());
        hitDto.setUri(hit.getUri());
        hitDto.setTimestamp(hit.getTimestamp());

        return hitDto;
    }

    public static Hit mapToHit(HitDto hitDto) {
        Hit hit = new Hit();

        hit.setApp(hitDto.getApp());
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setTimestamp(hitDto.getTimestamp());

        return hit;
    }
}
