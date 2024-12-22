package com.example.Warehouse.exceptions;

public class WarehouseAlreadyExistsException extends RuntimeException {
    public WarehouseAlreadyExistsException() { super("Склад уже существует"); }
}
