package com.rasberryspotify.core.repositories;

import com.rasberryspotify.core.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrackRepo extends JpaRepository<Track, UUID> {
}
