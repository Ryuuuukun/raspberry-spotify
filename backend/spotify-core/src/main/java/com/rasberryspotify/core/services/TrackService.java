package com.rasberryspotify.core.services;

import com.rasberryspotify.core.dto.track.TrackCreateRequest;
import com.rasberryspotify.core.dto.track.TrackResponse;
import com.rasberryspotify.core.dto.track.TrackUpdateRequest;
import com.rasberryspotify.core.entities.Track;
import com.rasberryspotify.core.entities.User;
import com.rasberryspotify.core.exception.AccessDeniedException;
import com.rasberryspotify.core.exception.NotFoundException;
import com.rasberryspotify.core.map.TrackMapper;
import com.rasberryspotify.core.repositories.TrackRepository;
import com.rasberryspotify.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final TrackMapper trackMapper;

    @Transactional
    public TrackResponse create(UUID currentUserId, TrackCreateRequest request) {
        User uploader = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Track track = Track.builder()
                .title(request.getTitle())
                .audioUrl(request.getAudioUrl())
                .coverArt(request.getCoverArt())
                .durationMs(request.getDurationMs())
                .releaseDate(request.getReleaseDate())
                .isPublic(Boolean.TRUE.equals(request.getIsPublic()))
                .lyrics(request.getLyrics())
                .uploadedBy(uploader)
                .build();

        return trackMapper.toResponse(trackRepository.save(track));
    }

    @Transactional(readOnly = true)
    public TrackResponse getById(UUID trackId, UUID currentUserId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

        if (!Boolean.TRUE.equals(track.getIsPublic())
                && (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(currentUserId))) {
            throw new AccessDeniedException("This track is private");
        }

        return trackMapper.toResponse(track);
    }

    @Transactional
    public TrackResponse update(UUID trackId, UUID currentUserId, TrackUpdateRequest request) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

        if (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can update this track");
        }

        trackMapper.updateTrackFromRequest(request, track);

        return trackMapper.toResponse(trackRepository.save(track));
    }


    @Transactional(readOnly = true)
    public List<TrackResponse> getMyTracks(UUID currentUserId) {
        return trackMapper.toResponses(trackRepository.findAllByUploadedById(currentUserId));
    }

    @Transactional
    public void delete(UUID trackId, UUID currentUserId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

        if (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can delete this track");
        }

        trackRepository.delete(track);
    }
}
