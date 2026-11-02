package com.rasberryspotify.backend.websocket;

import com.rasberryspotify.core.realtime.DeliveryEvent;
import com.rasberryspotify.core.realtime.DeliveryEventPublisher;
import com.rasberryspotify.core.realtime.DeliveryEventTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketDeliveryService implements DeliveryEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publish(DeliveryEventTarget target, DeliveryEvent event) {
        if (target.getType() == null) {
            throw new ArrayIndexOutOfBoundsException("asld;kjhf;lashdflkajshdflkahslkdfhaslkjdhf");
        }
        switch (target.getType()) {
            case USER -> messagingTemplate.convertAndSendToUser(target.getId(), target.getDestination(), event);
            case TOPIC -> messagingTemplate.convertAndSend(target.getId(), event);
        }
    }
}
