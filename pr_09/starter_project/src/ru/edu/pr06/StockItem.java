package ru.edu.pr06;

public class StockItem {
    private final String code;
    private final String name;
    private final Category category;
    private int quantity;

    public StockItem(String code, String name, Category category, int quantity) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code is blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is blank");
        }
        if (category == null) {
            throw new IllegalArgumentException("category is null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity < 0");
        }
        this.code = code;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public void increase(int q) {
        if (q <= 0) {
            throw new IllegalArgumentException("increase q <= 0");
        }
        if (Integer.MAX_VALUE - quantity < q) {
            throw new IllegalArgumentException("quantity overflow");
        }
        quantity += q;
    }

    public void decrease(int q) {
        if (q <= 0) {
            throw new IllegalArgumentException("decrease q <= 0");
        }
        if (q > quantity) {
            throw new IllegalArgumentException("insufficient quantity");
        }
        quantity -= q;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }
}
