package com.elementalsource.framework.dependencyinjection.impl;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;
import com.elementalsource.framework.dependencyinjection.sample.product.service.ProductService;
import com.elementalsource.framework.dependencyinjection.sample.product.service.ProductStockBuilder;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DependencyInjectionDefaultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldTakeRightImplementation() {
        // given
        final ProductStockBuilder productStockBuilder = new ProductStockBuilder();

        final Map<Class<?>, Object> map = ImmutableMap.<Class<?>, Object>builder()
            .put(ProductStockBuilder.class, productStockBuilder)
            .put(ProductService.class, new ProductService(productStockBuilder))
            .build();

        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);

        // when
        final ProductService bean = dependencyInjection.getBean(ProductService.class);

        // then
        assertNotNull(bean);
    }

    @Test
    public void shouldCountAllComponents() {
        // given
        final ProductStockBuilder productStockBuilder = new ProductStockBuilder();

        final Map<Class<?>, Object> map = ImmutableMap.<Class<?>, Object>builder()
            .put(ProductStockBuilder.class, productStockBuilder)
            .put(ProductService.class, new ProductService(productStockBuilder))
            .build();

        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);

        // when
        final Integer beansQuantity = dependencyInjection.getBeansQuantity();

        // then
        assertEquals(Integer.valueOf(2), beansQuantity);
    }

    @Test
    public void shouldThrowAnErrorWhenNotExistsImplementationForClass() {
        // given
        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(ImmutableMap.<Class<?>, Object>builder().build());
        class ClassThatCannotExists {

        }

        // expected exception
        // throw new ApplicationException("Dependency injection failure because " + object.getClass().getName() + " is not " + classBean.getName());
        expectedException.expect(ApplicationException.class);
        expectedException
            .expectMessage(containsString("Dependency injection failure because " + ClassThatCannotExists.class.getName() + " was not found"));

        // when
        dependencyInjection.getBean(ClassThatCannotExists.class);
    }

    @Test
    public void shouldThrowAnErrorWhenImplementationIsNotInstanceOfClass() {
        // given
        final Map<Class<?>, Object> map = ImmutableMap.<Class<?>, Object>builder()
            .put(ProductService.class, new Object())
            .build();
        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);

        // expected exception
        // throw new ApplicationException("Dependency injection failure because " + object.getClass().getName() + " is not " + classBean.getName());
        expectedException.expect(ApplicationException.class);
        expectedException
            .expectMessage(containsString(
                "Dependency injection failure because " + Object.class.getName() + " is not instance of " + ProductService.class.getName()));

        // when
        dependencyInjection.getBean(ProductService.class);
    }

}
