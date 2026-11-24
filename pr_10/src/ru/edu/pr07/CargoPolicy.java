package ru.edu.pr07;

public class CargoPolicy implements DeliveryPolicy {
    // Вариант 6: ограничение по весу 35 кг, граница включительная.
    private static final double MAX_WEIGHT_KG = 35.0;
    private static final double MIN_COST = 400.0;
    private static final double BASE_COST = 120.0;
    private static final double COST_PER_KM = 15.0;
    private static final double COST_PER_KG = 8.0;

    @Override
    public boolean supports(DeliveryRequest request) {
        return request != null
                && request.type() == DeliveryType.CARGO
                && request.distanceKm() > 0
                && request.weightKg() <= MAX_WEIGHT_KG;
    }

    @Override
    public double calculateCost(DeliveryRequest request) {
        if (!supports(request)) {
            throw new DeliveryUnavailableException(
                    "CARGO unavailable: weight must be <= " + MAX_WEIGHT_KG
                            + " kg and distance must be > 0"
            );
        }
        return Math.max(
                MIN_COST,
                BASE_COST + request.distanceKm() * COST_PER_KM + request.weightKg() * COST_PER_KG
        );
    }
}
