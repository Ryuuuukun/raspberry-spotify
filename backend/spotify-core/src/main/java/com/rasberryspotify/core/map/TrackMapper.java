package com.rasberryspotify.core.map;

import com.rasberryspotify.core.dto.track.TrackResponse;
import com.rasberryspotify.core.dto.track.TrackUpdateRequest;
import com.rasberryspotify.core.entities.Track;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TrackMapper {
    TrackResponse toResponse(Track track);
    List<TrackResponse> toResponses(List<Track> tracks);

    void updateTrackFromRequest(TrackUpdateRequest request, @MappingTarget Track track);
}
