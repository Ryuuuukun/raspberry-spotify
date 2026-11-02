package com.rasberryspotify.core.realtime;

public interface DeliveryEventPublisher {
    void publish(DeliveryEventTarget target, DeliveryEvent event);
}
