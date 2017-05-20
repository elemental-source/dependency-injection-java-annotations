package com.elementalsource.framework.dependencyinjection.sample;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import com.elementalsource.framework.dependencyinjection.impl.DependencyInjectionFactoryDefault;
import com.elementalsource.framework.dependencyinjection.sample.product.api.v1.controller.ProductController;

public class ProductApplication {

    public static void main(String... args) {
        final DependencyInjectionFactory dependencyInjectionFactory = DependencyInjectionFactoryDefault.getInstance();

        final DependencyInjection dependencyInjection = dependencyInjectionFactory.create();
        final ProductController productController = dependencyInjection
            .getBean(ProductController.class);

        System.out.println(productController.getPoducts());
    }

}
