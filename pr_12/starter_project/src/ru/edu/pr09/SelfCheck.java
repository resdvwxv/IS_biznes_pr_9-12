package ru.edu.pr09;

import java.util.*;

public class SelfCheck {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        SalesAnalyticsService service = new SalesAnalyticsService();

        List<Sale> sales = List.of(
                new Sale("S1", "Ivanov", "Moscow", 150000),
                new Sale("S2", "Petrova", "SPb", 92000),
                new Sale("S3", "Ivanov", "Moscow", 240000),
                new Sale("S4", "Lee", "Kazan", 45000)
        );

        // ---- topSales ----
        check("topSales min=100000 limit=2 returns 2 sales sorted by amount desc then id asc", () -> {
            List<Sale> result = service.topSales(sales, 100000, 2);
            assertEquals(2, result.size());
            assertEquals("S3", result.get(0).id());
            assertEquals("S1", result.get(1).id());
        });

        check("topSales min=240000 limit=1 includes boundary", () -> {
            List<Sale> result = service.topSales(sales, 240000, 1);
            assertEquals(1, result.size());
            assertEquals("S3", result.get(0).id());
        });

        check("topSales min=240001 limit=1 excludes boundary", () -> {
            List<Sale> result = service.topSales(sales, 240001, 1);
            assertEquals(0, result.size());
        });

        check("topSales with limit=0 returns empty", () -> {
            List<Sale> result = service.topSales(sales, 0, 0);
            assertEquals(0, result.size());
        });

        // ---- maxSale ----
        check("maxSale returns S3", () -> {
            Optional<Sale> max = service.maxSale(sales);
            assertTrue(max.isPresent());
            assertEquals("S3", max.get().id());
        });

        check("maxSale on empty list returns empty Optional", () -> {
            Optional<Sale> max = service.maxSale(List.of());
            assertTrue(max.isEmpty());
        });

        // ---- totalsByManager ----
        check("totalsByManager sums correctly", () -> {
            Map<String, Double> totals = service.totalsByManager(sales);
            assertEquals(390000.0, totals.get("Ivanov"), 0.001);
            assertEquals(92000.0, totals.get("Petrova"), 0.001);
            assertEquals(45000.0, totals.get("Lee"), 0.001);
        });

        // ---- averageByRegion ----
        check("averageByRegion Moscow = 195000", () -> {
            double avg = service.averageByRegion(sales, "Moscow");
            assertEquals(195000.0, avg, 0.001);
        });

        check("averageByRegion unknown region = 0.0", () -> {
            double avg = service.averageByRegion(sales, "Unknown");
            assertEquals(0.0, avg, 0.001);
        });

        check("averageByRegion null region throws IllegalArgumentException", () -> {
            assertThrows(IllegalArgumentException.class, () -> service.averageByRegion(sales, null));
        });

        check("averageByRegion blank region throws IllegalArgumentException", () -> {
            assertThrows(IllegalArgumentException.class, () -> service.averageByRegion(sales, "   "));
        });

        // ---- countLargeSales (вариант 6) ----
        check("countLargeSales returns 3 (35% of average)", () -> {
            long count = service.countLargeSales(sales);
            assertEquals(3L, count);
        });

        check("countLargeSales on empty list returns 0", () -> {
            long count = service.countLargeSales(List.of());
            assertEquals(0L, count);
        });

        // ---- валидация Sale ----
        check("Sale with negative amount throws IllegalArgumentException", () -> {
            assertThrows(IllegalArgumentException.class, () -> new Sale("S5", "Test", "Test", -1));
        });

        check("Sale with null id throws IllegalArgumentException", () -> {
            assertThrows(IllegalArgumentException.class, () -> new Sale(null, "Test", "Test", 100));
        });

        check("Sale with blank manager throws IllegalArgumentException", () -> {
            assertThrows(IllegalArgumentException.class, () -> new Sale("S5", "  ", "Test", 100));
        });

        // ---- исходная коллекция не изменяется ----
        check("original sales list size remains 4 after operations", () -> {
            assertEquals(4, sales.size());
        });

        System.out.println("PASSED=" + passed + " FAILED=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String description, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS: " + description);
        } catch (AssertionError e) {
            failed++;
            System.out.println("FAIL: " + description + " - " + e.getMessage());
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL: " + description + " - unexpected exception: "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected: " + expected + " but was: " + actual);
        }
    }

    private static void assertEquals(double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError("expected: " + expected + " but was: " + actual);
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("condition is false");
        }
    }

    private static void assertThrows(Class<? extends Throwable> expectedType, Runnable test) {
        try {
            test.run();
            throw new AssertionError("expected exception " + expectedType.getSimpleName() + " but none thrown");
        } catch (Throwable t) {
            if (!expectedType.isInstance(t)) {
                throw new AssertionError("expected exception " + expectedType.getSimpleName()
                        + " but got " + t.getClass().getSimpleName());
            }
        }
    }
}
