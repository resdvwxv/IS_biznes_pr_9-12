package ru.edu.pr07;

public class ExpressPolicy implements DeliveryPolicy {
    private static final double MAX_WEIGHT_KG = 10.0;
    private static final double BASE_COST = 250.0;
    private static final double COST_PER_KM = 25.0;
    private static final double COST_PER_KG = 12.0;

    @Override
    public boolean supports(DeliveryRequest request) {
        return request != null
                && request.type() == DeliveryType.EXPRESS
                && request.distanceKm() > 0
                && request.weightKg() <= MAX_WEIGHT_KG;
    }

    @Override
    public double calculateCost(DeliveryRequest request) {
        if (!supports(request)) {
            throw new DeliveryUnavailableException(
                    "EXPRESS unavailable: weight must be <= " + MAX_WEIGHT_KG
                            + " kg and distance must be > 0"
            );
        }
        return BASE_COST
                + request.distanceKm() * COST_PER_KM
                + request.weightKg() * COST_PER_KG;
    }
}
