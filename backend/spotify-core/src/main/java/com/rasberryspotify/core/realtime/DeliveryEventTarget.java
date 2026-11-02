package com.rasberryspotify.core.realtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class DeliveryEventTarget {
    private Type type;

    private String id;
    private String destination;

    public static DeliveryEventTarget user(String user, String destination) {
        return new DeliveryEventTarget(Type.USER, user, destination);
    }

    public static DeliveryEventTarget topic(String topic) {
        return new DeliveryEventTarget(Type.TOPIC, topic, null);
    }

    public static DeliveryEventTargetBuilder builder(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("The type cannot be null.");
        }

        return new DeliveryEventTargetBuilder().type(type);
    }

    public enum Type {
        USER, TOPIC
    }
}
