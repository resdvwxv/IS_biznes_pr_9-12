package ru.edu.pr07;

import java.util.Objects;

public class DeliveryService {
    private final DeliveryPolicyFactory factory;

    public DeliveryService(DeliveryPolicyFactory factory) {
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    public double calculate(DeliveryRequest request) {
        Objects.requireNonNull(request, "request");

        DeliveryPolicy policy = factory.get(request.type());
        if (!policy.supports(request)) {
            throw new DeliveryUnavailableException(
                    "Delivery " + request.type() + " unavailable: "
                            + "distanceKm=" + request.distanceKm()
                            + ", weightKg=" + request.weightKg()
            );
        }
        return policy.calculateCost(request);
    }
}
