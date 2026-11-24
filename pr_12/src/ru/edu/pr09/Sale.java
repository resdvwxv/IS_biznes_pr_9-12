package ru.edu.pr09;

public record Sale(String id, String manager, String region, double amount) {
    public Sale {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank");
        }
        if (manager == null || manager.isBlank()) {
            throw new IllegalArgumentException("manager must not be null or blank");
        }
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException("region must not be null or blank");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
    }
}
