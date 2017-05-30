package com.elementalsource.framework.dependencyinjection.sample.product.api.v1.controller;

import com.elementalsource.framework.dependencyinjection.Component;
import com.elementalsource.framework.dependencyinjection.Inject;
import com.elementalsource.framework.dependencyinjection.sample.product.model.Product;
import com.elementalsource.framework.dependencyinjection.sample.product.service.ProductService;

@Component
public class ProductController {

    private final ProductService productService;

    @Inject
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    public Iterable<Product> getPoducts() {
        return productService.getAll();
    }

}
