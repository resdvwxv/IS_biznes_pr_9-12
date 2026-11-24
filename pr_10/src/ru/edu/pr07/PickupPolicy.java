package ru.edu.pr07;

public class PickupPolicy implements DeliveryPolicy {
    @Override
    public boolean supports(DeliveryRequest request) {
        return request != null
                && request.type() == DeliveryType.PICKUP
                && request.distanceKm() == 0.0;
    }

    @Override
    public double calculateCost(DeliveryRequest request) {
        if (!supports(request)) {
            throw new DeliveryUnavailableException(
                    "PICKUP unavailable: distance must be 0 for pickup"
            );
        }
        return 0.0;
    }
}
