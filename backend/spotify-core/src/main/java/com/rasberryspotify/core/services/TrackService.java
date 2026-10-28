package com.rasberryspotify.core.services;

import com.rasberryspotify.core.dto.track.TrackCreateRequest;
import com.rasberryspotify.core.dto.track.TrackResponse;
import com.rasberryspotify.core.dto.track.TrackUpdateRequest;
import com.rasberryspotify.core.entities.Track;
import com.rasberryspotify.core.entities.User;
import com.rasberryspotify.core.exception.AccessDeniedException;
import com.rasberryspotify.core.exception.NotFoundException;
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
    private final TrackRepository trackRepo;
    private final UserRepository userRepo;

    @Transactional
    public TrackResponse create(UUID currentUserId, TrackCreateRequest request) {
        User uploader = userRepo.findById(currentUserId)
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

        trackRepo.save(track);
        return toResponse(track);
    }

    @Transactional(readOnly = true)
    public TrackResponse getById(UUID trackId, UUID currentUserId) {
        Track track = trackRepo.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

        if (!Boolean.TRUE.equals(track.getIsPublic())
                && (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(currentUserId))) {
            throw new AccessDeniedException("This track is private");
        }

        return toResponse(track);
    }

    @Transactional
    public TrackResponse update(UUID trackId, UUID currentUserId, TrackUpdateRequest request) {
        Track track = trackRepo.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

        if (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can update this track");
        }

        if (request.getTitle() != null) track.setTitle(request.getTitle());
        if (request.getAudioUrl() != null) track.setAudioUrl(request.getAudioUrl());
        if (request.getCoverArt() != null) track.setCoverArt(request.getCoverArt());
        if (request.getDurationMs() != null) track.setDurationMs(request.getDurationMs());
        if (request.getReleaseDate() != null) track.setReleaseDate(request.getReleaseDate());
        if (request.getIsPublic() != null) track.setIsPublic(request.getIsPublic());
        if (request.getLyrics() != null) track.setLyrics(request.getLyrics());

        trackRepo.save(track);
        return toResponse(track);
    }


    private TrackResponse toResponse(Track track) {
        return TrackResponse.builder()
                .id(track.getId())
                .title(track.getTitle())
                .audioUrl(track.getAudioUrl())
                .coverArt(track.getCoverArt())
                .durationMs(track.getDurationMs())
                .releaseDate(track.getReleaseDate())
                .isPublic(track.getIsPublic())
                .lyrics(track.getLyrics())
                .uploadedById(track.getUploadedBy() != null ? track.getUploadedBy().getId() : null)
                .uploadedByUsername(track.getUploadedBy() != null ? track.getUploadedBy().getUsername() : null)
                .createdAt(track.getCreatedAt())
                .updatedAt(track.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TrackResponse> getMyTracks(UUID currentUserId) {
        return trackRepo.findAllByUploadedById(currentUserId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(UUID trackId, UUID currentUserId) {
        Track track = trackRepo.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

        if (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can delete this track");
        }

        trackRepo.delete(track);
    }
}
