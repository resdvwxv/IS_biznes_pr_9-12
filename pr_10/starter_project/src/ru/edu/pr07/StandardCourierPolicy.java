package ru.edu.pr07;

public class StandardCourierPolicy implements DeliveryPolicy {
    private static final double MAX_WEIGHT_KG = 20.0;
    private static final double MIN_COST = 150.0;
    private static final double BASE_COST = 80.0;
    private static final double COST_PER_KM = 10.0;
    private static final double COST_PER_KG = 6.0;

    @Override
    public boolean supports(DeliveryRequest request) {
        return request != null
                && request.type() == DeliveryType.STANDARD
                && request.distanceKm() > 0
                && request.weightKg() <= MAX_WEIGHT_KG;
    }

    @Override
    public double calculateCost(DeliveryRequest request) {
        if (!supports(request)) {
            throw new DeliveryUnavailableException(
                    "STANDARD unavailable: weight must be <= " + MAX_WEIGHT_KG
                            + " kg and distance must be > 0"
            );
        }
        return Math.max(
                MIN_COST,
                BASE_COST + request.distanceKm() * COST_PER_KM + request.weightKg() * COST_PER_KG
        );
    }
}
