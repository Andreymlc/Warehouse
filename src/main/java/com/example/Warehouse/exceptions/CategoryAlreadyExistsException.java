package com.example.Warehouse.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException() { super("Категория уже существует"); }
}
