package com.elementalsource.framework.dependencyinjection.sample;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.impl.ComponentRepositoryDefault;
import com.elementalsource.framework.dependencyinjection.impl.DependencyInjectionFactoryDefault;
import com.elementalsource.framework.dependencyinjection.sample.product.api.v1.controller.ProductController;
import java.util.Set;

public class ProductApplication {

    public static void main(String... args) {
        final Set<Class<?>> allComponents = ComponentRepositoryDefault.getInstance().getAllComponents();













        final DependencyInjection dependencyInjection = DependencyInjectionFactoryDefault.getInstance().create(allComponents);
        final ProductController productController = dependencyInjection
            .getBean(ProductController.class);

        System.out.println(productController.getPoducts());
    }

}
