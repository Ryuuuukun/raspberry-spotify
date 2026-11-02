package com.rasberryspotify.core.dto.websockets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WebsocketNotification {
    private String username;
    private Object message;
}
