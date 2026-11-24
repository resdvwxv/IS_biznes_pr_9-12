package ru.edu.pr07;

import java.util.EnumMap;
import java.util.Map;

public class DeliveryPolicyFactory {
    private final Map<DeliveryType, DeliveryPolicy> policies = new EnumMap<>(DeliveryType.class);

    public DeliveryPolicyFactory() {
        policies.put(DeliveryType.STANDARD, new StandardCourierPolicy());
        policies.put(DeliveryType.EXPRESS, new ExpressPolicy());
        policies.put(DeliveryType.PICKUP, new PickupPolicy());
        policies.put(DeliveryType.CARGO, new CargoPolicy());
    }

    public DeliveryPolicy get(DeliveryType type) {
        if (type == null) {
            throw new IllegalArgumentException("type is required");
        }
        DeliveryPolicy policy = policies.get(type);
        if (policy == null) {
            throw new IllegalArgumentException("No DeliveryPolicy registered for " + type);
        }
        return policy;
    }
}
