package com.rasberryspotify.core.repositories;

import com.rasberryspotify.core.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaylistRepo extends JpaRepository<Playlist, UUID> {
}
