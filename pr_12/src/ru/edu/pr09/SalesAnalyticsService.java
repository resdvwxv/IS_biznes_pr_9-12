package ru.edu.pr09;

import java.util.*;
import java.util.stream.*;

public class SalesAnalyticsService {
    private static final double LARGE_SALE_PERCENT = 35.0;

    public List<Sale> topSales(List<Sale> sales, double min, int limit) {
        if (sales == null) {
            throw new IllegalArgumentException("sales must not be null");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit must be non-negative");
        }
        return sales.stream()
                .filter(s -> s.amount() >= min)
                .sorted(Comparator.comparingDouble(Sale::amount).reversed()
                        .thenComparing(Sale::id))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Optional<Sale> maxSale(List<Sale> sales) {
        if (sales == null) {
            throw new IllegalArgumentException("sales must not be null");
        }
        return sales.stream()
                .max(Comparator.comparingDouble(Sale::amount));
    }

    public Map<String, Double> totalsByManager(List<Sale> sales) {
        if (sales == null) {
            throw new IllegalArgumentException("sales must not be null");
        }
        return sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::manager,
                        Collectors.summingDouble(Sale::amount)
                ));
    }

    public double averageByRegion(List<Sale> sales, String region) {
        if (sales == null) {
            throw new IllegalArgumentException("sales must not be null");
        }
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException("region must not be null or blank");
        }
        return sales.stream()
                .filter(s -> region.equals(s.region()))
                .mapToDouble(Sale::amount)
                .average()
                .orElse(0.0);
    }

    // Вариант 6: дополнительный агрегат — количество продаж,
    // сумма которых превышает 35 % от средней суммы всех продаж.
    public long countLargeSales(List<Sale> sales) {
        if (sales == null) {
            throw new IllegalArgumentException("sales must not be null");
        }
        if (sales.isEmpty()) {
            return 0;
        }
        double average = sales.stream()
                .mapToDouble(Sale::amount)
                .average()
                .orElse(0.0);
        double threshold = average * LARGE_SALE_PERCENT / 100.0;
        return sales.stream()
                .filter(s -> s.amount() > threshold)
                .count();
    }
}
