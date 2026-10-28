package com.rasberryspotify.core.repositories;

import com.rasberryspotify.core.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
    List<Playlist> findAllByOwnerId(UUID ownerId);
}
