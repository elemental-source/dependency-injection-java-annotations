package com.elementalsource.framework.dependencyinjection.sample.product.model;

public class Product {

    private final String description;

    public Product(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Product: " + description;
    }
}
