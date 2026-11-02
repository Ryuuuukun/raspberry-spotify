package com.rasberryspotify.core.realtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeliveryEvent {
    private Type type;

    public enum Type {
        PLAYLIST_UPDATE,
        PLAYLIST_CREATE,
        PLAYLIST_DELETE,
        LIBRARY_UPDATE
    }
}
