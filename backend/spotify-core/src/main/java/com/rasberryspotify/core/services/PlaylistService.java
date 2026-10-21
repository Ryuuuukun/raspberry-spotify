package com.rasberryspotify.core.services;

import com.rasberryspotify.core.dto.playlist.PlaylistResponse;
import com.rasberryspotify.core.dto.playlist.PlaylistTrackResponse;
import com.rasberryspotify.core.dto.playlist.PlaylistCreateRequest;
import com.rasberryspotify.core.dto.playlist.PlaylistUpdateRequest;
import com.rasberryspotify.core.entities.User;
import com.rasberryspotify.core.entities.Playlist;
import com.rasberryspotify.core.entities.PlaylistTrack;
import com.rasberryspotify.core.entities.Track;
import com.rasberryspotify.core.exception.AccessDeniedException;
import com.rasberryspotify.core.exception.NotFoundException;
import com.rasberryspotify.core.repositories.PlaylistRepo;
import com.rasberryspotify.core.repositories.PlaylistTrackRepo;
import com.rasberryspotify.core.repositories.TrackRepo;
import com.rasberryspotify.core.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepo playlistRepo;
    private final UserRepo userRepo;
    private final PlaylistTrackRepo playlistTrackRepo;
    private final TrackRepo trackRepo;

    @Transactional
    public PlaylistResponse create(UUID currentUserId, PlaylistCreateRequest request) {
        User owner = userRepo.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found: " + currentUserId));

        Playlist playlist = Playlist.builder()
                .owner(owner)
                .title(request.getTitle())
                .description(request.getDescription())
                .coverArt(request.getCoverArt())
                .visibility(request.getVisibility() != null ? request.getVisibility() : "private")
                .build();

        playlistRepo.save(playlist);

        if (request.getTrackIds() != null && !request.getTrackIds().isEmpty()) {
            int position = 0;
            for (UUID trackId : request.getTrackIds()) {
                Track track = trackRepo.findById(trackId)
                        .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

                if (!Boolean.TRUE.equals(track.getIsPublic())
                        && (track.getUploadedBy() == null
                        || !track.getUploadedBy().getId().equals(currentUserId))) {
                    throw new AccessDeniedException(
                            "Cannot add private track of another user: " + trackId);
                }

                PlaylistTrack pt = PlaylistTrack.builder()
                        .playlist(playlist)
                        .track(track)
                        .position(position++)
                        .addedBy(owner)
                        .build();
                playlistTrackRepo.save(pt);
            }
        }

        return toResponse(playlist);
    }

    @Transactional
    public PlaylistResponse update(UUID playlistId, UUID currentUserId, PlaylistUpdateRequest request) {
        Playlist playlist = playlistRepo.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist not found: " + playlistId));

        if (!playlist.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can update this playlist");
        }

        if (request.getTitle() != null) playlist.setTitle(request.getTitle());
        if (request.getDescription() != null) playlist.setDescription(request.getDescription());
        if (request.getCoverArt() != null) playlist.setCoverArt(request.getCoverArt());
        if (request.getVisibility() != null) playlist.setVisibility(request.getVisibility());

        playlistRepo.save(playlist);
        return toResponse(playlist);
    }

    @Transactional
    public void delete(UUID playlistId, UUID currentUserId) {
        Playlist playlist = playlistRepo.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist not found: " + playlistId));

        if (!playlist.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can delete this playlist");
        }

        playlistRepo.delete(playlist);
    }

    @Transactional(readOnly = true)
    public PlaylistResponse getById(UUID playlistId, UUID currentUserId){
        Playlist playlist = playlistRepo.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist not found: " + playlistId));

        if ("private".equals(playlist.getVisibility())
                && !playlist.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("This playlist is private");
        }

        return toResponse(playlist);
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> getByOwner(UUID ownerId, UUID currentUserId) {
        List<Playlist> playlists = playlistRepo.findAllByOwnerId(ownerId);

        boolean isOwner = ownerId.equals(currentUserId);

        return playlists.stream()
                .filter(p -> isOwner || !"private".equals(p.getVisibility()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PlaylistResponse addTrack(UUID playlistId, UUID trackId, UUID currentUserId) {
        Playlist playlist = playlistRepo.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist not found: " + playlistId));

        if (!playlist.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can modify this playlist");
        }

        Track track = trackRepo.findById(trackId)
                .orElseThrow(() -> new NotFoundException("Track not found: " + trackId));

        if (!Boolean.TRUE.equals(track.getIsPublic())
                && (track.getUploadedBy() == null
                || !track.getUploadedBy().getId().equals(currentUserId))) {
            throw new AccessDeniedException("Cannot add private track of another user");
        }

        List<PlaylistTrack> existing = playlistTrackRepo
                .findAllByPlaylistIdOrderByPositionAsc(playlistId);

        boolean alreadyPresent = existing.stream()
                .anyMatch(pt -> pt.getTrack().getId().equals(trackId));
        if (alreadyPresent) {
            throw new IllegalStateException("Track already in playlist: " + trackId);
        }

        int nextPosition = existing.stream()
                .mapToInt(PlaylistTrack::getPosition)
                .max()
                .orElse(-1) + 1;

        PlaylistTrack pt = PlaylistTrack.builder()
                .playlist(playlist)
                .track(track)
                .position(nextPosition)
                .addedBy(playlist.getOwner())
                .build();

        playlistTrackRepo.save(pt);

        return toResponse(playlist);
    }

    @Transactional
    public PlaylistResponse removeTrack(UUID playlistId, UUID trackId, UUID currentUserId) {
        Playlist playlist = playlistRepo.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist not found: " + playlistId));

        if (!playlist.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can modify this playlist");
        }

        List<PlaylistTrack> items = playlistTrackRepo
                .findAllByPlaylistIdOrderByPositionAsc(playlistId);

        PlaylistTrack target = items.stream()
                .filter(pt -> pt.getTrack().getId().equals(trackId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Track not in this playlist: " + trackId));

        playlistTrackRepo.delete(target);

        items.remove(target);
        int newPos = 0;
        for (PlaylistTrack pt : items) {
            pt.setPosition(newPos++);
        }
        playlistTrackRepo.saveAll(items);

        return toResponse(playlist);
    }

    private PlaylistResponse toResponse(Playlist playlist) {
        List<PlaylistTrack> items = playlistTrackRepo
                .findAllByPlaylistIdOrderByPositionAsc(playlist.getId());

        List<PlaylistTrackResponse> trackResponses = items.stream()
                .map(pt -> PlaylistTrackResponse.builder()
                        .trackId(pt.getTrack().getId())
                        .title(pt.getTrack().getTitle())
                        .coverArt(pt.getTrack().getCoverArt())
                        .durationMs(pt.getTrack().getDurationMs())
                        .position(pt.getPosition())
                        .build())
                .toList();

        return PlaylistResponse.builder()
                .id(playlist.getId())
                .title(playlist.getTitle())
                .description(playlist.getDescription())
                .coverArt(playlist.getCoverArt())
                .visibility(playlist.getVisibility())
                .ownerId(playlist.getOwner().getId())
                .ownerUsername(playlist.getOwner().getUsername())
                .tracks(trackResponses)
                .createdAt(playlist.getCreatedAt())
                .updatedAt(playlist.getUpdatedAt())
                .build();
    }
}
