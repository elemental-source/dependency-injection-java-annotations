package com.elementalsource.framework.dependencyinjection.sample.product.service;

import com.elementalsource.framework.dependencyinjection.annotation.Component;
import com.elementalsource.framework.dependencyinjection.sample.product.model.Product;
import com.google.common.collect.ImmutableList;

@Component
public class ProductStockBuilder {

    public Iterable<Product> build() {
        return ImmutableList.<Product>builder()
            .add(new Product("Beer"))
            .add(new Product("More beer"))
            .add(new Product("And more Beer"))
            .build();
    }

}
