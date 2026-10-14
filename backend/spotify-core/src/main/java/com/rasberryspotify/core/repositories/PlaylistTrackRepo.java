package com.rasberryspotify.core.repositories;

import com.rasberryspotify.core.entities.PlaylistTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistTrackRepo extends JpaRepository<PlaylistTrack, UUID> {
    List<PlaylistTrack> findAllByPlaylistIdOrderByPositionAsc(UUID playlistId);
}
