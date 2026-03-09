package ru.practicum.locationModel.mapper;

import ru.practicum.LocationDto;
import ru.practicum.locationModel.Location;

public class LocationMapper {

    public static Location mapToLocation(LocationDto dto) {
        Location location = new Location();

        location.setLat(dto.getLat());
        location.setLon(dto.getLon());

        return location;
    }

    public static LocationDto mapToLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();

        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());

        return locationDto;
    }
}
