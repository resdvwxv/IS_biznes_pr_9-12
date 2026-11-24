package ru.edu.pr07;

public class DeliveryUnavailableException extends IllegalArgumentException {
    public DeliveryUnavailableException(String message) {
        super(message);
    }
}
