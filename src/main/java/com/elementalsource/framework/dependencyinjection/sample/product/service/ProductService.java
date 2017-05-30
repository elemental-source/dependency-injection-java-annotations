package com.elementalsource.framework.dependencyinjection.sample.product.service;

import com.elementalsource.framework.dependencyinjection.Component;
import com.elementalsource.framework.dependencyinjection.Inject;
import com.elementalsource.framework.dependencyinjection.sample.product.model.Product;

@Component
public class ProductService {

    private final ProductStockBuilder productStockBuilder;

    @Inject
    public ProductService(final ProductStockBuilder productStockBuilder) {
        this.productStockBuilder = productStockBuilder;
    }

    public Iterable<Product> getAll() {
        return productStockBuilder.build();
    }

}
