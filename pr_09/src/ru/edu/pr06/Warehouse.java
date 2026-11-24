package ru.edu.pr06;

import java.util.*;

public class Warehouse {
    public static final int MAX_ISSUE_PER_OPERATION = 35;

    private final Map<String, StockItem> items = new HashMap<>();
    private final List<StockMovement> movements = new ArrayList<>();
    private final Map<Category, Integer> totals = new EnumMap<>(Category.class);

    public void addItem(StockItem item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
        if (items.containsKey(item.getCode())) {
            throw new IllegalArgumentException("duplicate code: " + item.getCode());
        }

        StockItem stored = copyOf(item);
        items.put(stored.getCode(), stored);
        totals.merge(stored.getCategory(), stored.getQuantity(), Integer::sum);
    }

    public void receive(String code, int q) {
        if (q <= 0) {
            throw new IllegalArgumentException("receive q <= 0");
        }

        StockItem item = requireItem(code);
        item.increase(q);
        totals.merge(item.getCategory(), q, Integer::sum);
        movements.add(new StockMovement(code, MovementType.RECEIVE, q));
    }

    public void issue(String code, int q) {
        if (q <= 0) {
            throw new IllegalArgumentException("issue q <= 0");
        }
        if (q > MAX_ISSUE_PER_OPERATION) {
            throw new IllegalArgumentException(
                    "issue q > " + MAX_ISSUE_PER_OPERATION + " per operation"
            );
        }

        StockItem item = requireItem(code);
        item.decrease(q);
        totals.merge(item.getCategory(), -q, Integer::sum);
        movements.add(new StockMovement(code, MovementType.ISSUE, q));
    }

    public List<StockItem> findByCategory(Category c) {
        if (c == null) {
            throw new IllegalArgumentException("category is null");
        }

        List<StockItem> result = new ArrayList<>();
        for (StockItem item : items.values()) {
            if (item.getCategory() == c) {
                result.add(copyOf(item));
            }
        }
        return result;
    }

    public Map<Category, Integer> totalsByCategory() {
        return new EnumMap<>(totals);
    }

    public Set<Category> categories() {
        return new HashSet<>(totals.keySet());
    }

    public List<StockMovement> movements() {
        return List.copyOf(movements);
    }

    private StockItem requireItem(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code is blank");
        }
        StockItem item = items.get(code);
        if (item == null) {
            throw new IllegalArgumentException("unknown code: " + code);
        }
        return item;
    }

    private StockItem copyOf(StockItem item) {
        return new StockItem(
                item.getCode(),
                item.getName(),
                item.getCategory(),
                item.getQuantity()
        );
    }
}
