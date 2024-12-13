package com.example.Warehouse.domain.enums;

/**
 * Статусы заказа
 * <p>{@code CREATED} - Создан
 * <p>{@code CONFIRMED} - Подтверждён
 * <p>{@code SHIPPED} - Отправлен
 * <p>{@code DELIVERED} - Доставлен
 * <p>{@code CANCELED} - Отменён
 * */
public enum Status {
    CREATED,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELED;

    public Status next() {
        int currentIndex = this.ordinal();

        if (currentIndex < Status.values().length - 1) {
            return Status.values()[currentIndex + 1];
        } else {
            throw new IllegalStateException("Нельзя перейти дальше");
        }
    }
}
