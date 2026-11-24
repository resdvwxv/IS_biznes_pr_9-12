package ru.edu.pr06;

import java.util.*;

public class SelfCheck {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        check("receive/issue updates quantity, totals and movements", SelfCheck::testReceiveIssue);
        check("issue exactly 35 is allowed (variant 6 upper bound)", SelfCheck::testIssueUpperBound);
        check("issue 36 is rejected (variant 6)", () -> expectThrows(IllegalArgumentException.class, () -> {
            Warehouse w = new Warehouse();
            w.addItem(new StockItem("BULK-1", "Bulk", Category.COMPUTER, 100));
            w.issue("BULK-1", 36);
        }));
        check("non-positive receive/issue is rejected", () -> {
            Warehouse w = new Warehouse();
            w.addItem(new StockItem("A", "A", Category.NETWORK, 10));
            expectThrows(IllegalArgumentException.class, () -> w.receive("A", 0));
            expectThrows(IllegalArgumentException.class, () -> w.issue("A", -1));
        });
        check("duplicate code is rejected", () -> {
            Warehouse w = new Warehouse();
            w.addItem(new StockItem("D", "D", Category.DISPLAY, 1));
            expectThrows(IllegalArgumentException.class,
                    () -> w.addItem(new StockItem("D", "D2", Category.DISPLAY, 1)));
        });
        check("issue more than stock is rejected", () -> {
            Warehouse w = new Warehouse();
            w.addItem(new StockItem("S", "S", Category.PERIPHERAL, 5));
            expectThrows(IllegalArgumentException.class, () -> w.issue("S", 6));
        });
        check("unknown code is rejected", () -> {
            Warehouse w = new Warehouse();
            expectThrows(IllegalArgumentException.class, () -> w.receive("NOPE", 1));
            expectThrows(IllegalArgumentException.class, () -> w.issue("NOPE", 1));
        });
        check("findByCategory returns independent list and copies", SelfCheck::testDefensiveCopies);
        check("totals and categories return copies", SelfCheck::testTotalsCopies);
        check("movements list is unmodifiable copy", SelfCheck::testMovementsCopy);

        System.out.println("PASSED=" + passed + " FAILED=" + failed);
        System.out.println(failed == 0 ? "PASS" : "FAIL");
    }

    private static void testReceiveIssue() {
        Warehouse w = new Warehouse();
        w.addItem(new StockItem("NB-01", "Notebook", Category.COMPUTER, 12));

        w.receive("NB-01", 3);
        w.issue("NB-01", 2);

        if (w.totalsByCategory().get(Category.COMPUTER) != 13) {
            throw new AssertionError("totals");
        }
        if (w.movements().size() != 2) {
            throw new AssertionError("movements size");
        }
        if (w.movements().get(0).type() != MovementType.RECEIVE) {
            throw new AssertionError("movement type 1");
        }
        if (w.movements().get(1).type() != MovementType.ISSUE) {
            throw new AssertionError("movement type 2");
        }
    }

    private static void testIssueUpperBound() {
        Warehouse w = new Warehouse();
        w.addItem(new StockItem("BULK-2", "Bulk", Category.COMPUTER, 40));

        w.issue("BULK-2", Warehouse.MAX_ISSUE_PER_OPERATION);

        if (w.totalsByCategory().get(Category.COMPUTER) != 5) {
            throw new AssertionError("expected 5");
        }
    }

    private static void testDefensiveCopies() {
        Warehouse w = new Warehouse();

        StockItem original = new StockItem("C-1", "C1", Category.COMPUTER, 10);
        w.addItem(original);
        w.addItem(new StockItem("C-2", "C2", Category.COMPUTER, 20));

        original.increase(100);

        List<StockItem> list = w.findByCategory(Category.COMPUTER);
        if (list.size() != 2) {
            throw new AssertionError("size");
        }

        StockItem copy = list.get(0);
        copy.increase(100);
        list.clear();

        if (w.findByCategory(Category.COMPUTER).size() != 2) {
            throw new AssertionError("internal list changed");
        }
        if (w.totalsByCategory().get(Category.COMPUTER) != 30) {
            throw new AssertionError("internal item changed");
        }
    }

    private static void testTotalsCopies() {
        Warehouse w = new Warehouse();
        w.addItem(new StockItem("N-1", "N1", Category.NETWORK, 4));
        w.addItem(new StockItem("D-1", "D1", Category.DISPLAY, 6));

        Map<Category, Integer> totals = w.totalsByCategory();
        totals.clear();

        if (w.totalsByCategory().isEmpty()) {
            throw new AssertionError("totals exposed");
        }

        Set<Category> cats = w.categories();
        cats.clear();

        if (w.categories().size() != 2) {
            throw new AssertionError("categories exposed");
        }
        if (!w.categories().containsAll(Set.of(Category.NETWORK, Category.DISPLAY))) {
            throw new AssertionError("categories content");
        }
    }

    private static void testMovementsCopy() {
        Warehouse w = new Warehouse();
        w.addItem(new StockItem("M-1", "M1", Category.PERIPHERAL, 10));
        w.receive("M-1", 1);

        List<StockMovement> m = w.movements();
        try {
            m.clear();
            throw new AssertionError("movements mutable");
        } catch (UnsupportedOperationException expected) {
            // ok
        }

        if (w.movements().size() != 1) {
            throw new AssertionError("movements changed");
        }
    }

    private static void check(String name, Runnable r) {
        try {
            r.run();
            passed++;
            System.out.println("PASS " + name);
        } catch (Throwable t) {
            failed++;
            System.out.println("FAIL " + name + ": " + t.getMessage());
        }
    }

    private static void expectThrows(Class<? extends Throwable> expected, Runnable r) {
        try {
            r.run();
        } catch (Throwable t) {
            if (expected.isInstance(t)) {
                return;
            }
            throw new AssertionError("wrong exception: " + t);
        }
        throw new AssertionError("expected " + expected.getSimpleName());
    }
}
