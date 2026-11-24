package ru.edu.pr06;

public record StockMovement(String itemCode, MovementType type, int quantity) {
    public StockMovement {
        if (itemCode == null || itemCode.isBlank()) {
            throw new IllegalArgumentException("itemCode is blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("movement quantity <= 0");
        }
    }
}
