package com.rasberryspotify.core.services;

import com.rasberryspotify.core.dto.playlist.PlaylistCreateRequest;
import com.rasberryspotify.core.dto.playlist.PlaylistResponse;
import com.rasberryspotify.core.dto.playlist.PlaylistUpdateRequest;
import com.rasberryspotify.core.entities.Playlist;
import com.rasberryspotify.core.exception.AccessDeniedException;
import com.rasberryspotify.core.exception.NotFoundException;
import com.rasberryspotify.core.map.PlaylistMapper;
import com.rasberryspotify.core.repositories.PlaylistRepository;
import com.rasberryspotify.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.PreprocessedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;

    private final PlaylistMapper playlistMapper;

    @Transactional
    public PlaylistResponse create(UUID ownerId, PlaylistCreateRequest request) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User was not found."));
        var playlist = Playlist.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .visibility(request.getVisibility())
                .owner(owner)
                .build();

        return playlistMapper.toResponse(playlistRepository.save(playlist));
    }

    @Transactional
    public PlaylistResponse update(UUID playlistId, UUID currentUserId, PlaylistUpdateRequest request) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException("Playlist was not found."));

        if (!playlist.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can update this playlist");
        }

        playlistMapper.updatePlaylistFromRequest(request, playlist);
        return playlistMapper.toResponse(playlistRepository.save(playlist));
    }

    @Transactional
    public void delete(UUID playlistId, UUID currentUserId) {
        var playlist = playlistRepository.findById(playlistId)
                        .orElseThrow(() -> new NotFoundException("Playlist was not found."));

        if (!playlist.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the owner can delete this playlist");
        }

        playlistRepository.delete(playlist);
    }

    @Transactional(readOnly = true)
    public PlaylistResponse getById(UUID playlistId) {
        return playlistRepository.findById(playlistId)
                .map(playlistMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Playlist was not found"));
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> getByOwner(UUID ownerId) {
        return playlistMapper.toResponses(playlistRepository.findAllByOwnerId(ownerId));
    }

    @Transactional(readOnly = true)
    public Page<PlaylistResponse> getAll(Pageable pageable) {
        return playlistRepository.findAll(pageable)
                .map(playlistMapper::toResponse);
    }
}
