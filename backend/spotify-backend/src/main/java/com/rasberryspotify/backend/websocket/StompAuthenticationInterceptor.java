package com.rasberryspotify.backend.websocket;

import com.rasberryspotify.backend.security.KeycloakAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthenticationInterceptor implements ChannelInterceptor {
    private final KeycloakAuthenticationConverter jwtAuthenticationConverter;
    private final JwtDecoder jwtDecoder;

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            var authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                var token = authHeader.substring(7);
                try {
                    var jwt = jwtDecoder.decode(token);
                    var authentication = jwtAuthenticationConverter.convert(jwt);

                    accessor.setUser(authentication);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid Keycloak token", e);
                }
            } else {
                throw new IllegalArgumentException("Missing Authorization header");
            }
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }
}
