package ru.edu.pr07;

public record DeliveryRequest(
        DeliveryType type,
        double distanceKm,
        double weightKg,
        double orderAmount
) {
    public DeliveryRequest {
        if (type == null) {
            throw new IllegalArgumentException("type is required");
        }
        if (!Double.isFinite(distanceKm) || distanceKm < 0) {
            throw new IllegalArgumentException("distanceKm must be finite and >= 0");
        }
        if (!Double.isFinite(weightKg) || weightKg <= 0) {
            throw new IllegalArgumentException("weightKg must be finite and > 0");
        }
        if (!Double.isFinite(orderAmount) || orderAmount < 0) {
            throw new IllegalArgumentException("orderAmount must be finite and >= 0");
        }
    }
}
