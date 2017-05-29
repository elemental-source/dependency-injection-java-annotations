package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.Component;
import com.elementalsource.framework.dependencyinjection.ComponentReference;
import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;
import com.elementalsource.framework.dependencyinjection.sample.product.service.ProductService;
import com.elementalsource.framework.dependencyinjection.sample.product.service.ProductStockBuilder;
import com.google.common.collect.ImmutableMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class DependencyInjectionDefaultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldTakeRightImplementation() {
        // given
        final ProductStockBuilder productStockBuilder = new ProductStockBuilder();
        final ProductService bean = new ProductService(productStockBuilder);

        final Map<ComponentReference, Object> map = ImmutableMap.<ComponentReference, Object>builder()
            .put(new ComponentReferenceDefault(ProductStockBuilder.class), productStockBuilder)
            .put(new ComponentReferenceDefault(ProductService.class), bean)
            .build();

        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);

        // when
        final ProductService result = dependencyInjection.getBean(ProductService.class);

        // then
        assertNotNull(result);
        assertSame(bean, result);
        assertNotSame(new ProductService(productStockBuilder), result);
    }

    private interface MyComponent {

    }

    @Test
    public void shouldTakeImplementationByInterface() {
        @Component
        class MyComponentImpl implements MyComponent {

        }
        // given
        final MyComponentImpl bean = new MyComponentImpl();
        final Map<ComponentReference, Object> map = ImmutableMap.<ComponentReference, Object>builder()
            .put(new ComponentReferenceDefault(MyComponentImpl.class), bean)
            .build();

        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);

        // when
        final MyComponent result = dependencyInjection.getBean(MyComponent.class);

        // then
        assertNotNull(result);
        assertSame(bean, result);
        assertNotSame(new MyComponentImpl(), result);
    }

    @Test
    public void shouldCountAllComponents() {
        // given
        final ProductStockBuilder productStockBuilder = new ProductStockBuilder();

        final Map<ComponentReference, Object> map = ImmutableMap.<ComponentReference, Object>builder()
            .put(new ComponentReferenceDefault(ProductStockBuilder.class), productStockBuilder)
            .put(new ComponentReferenceDefault(ProductService.class), new ProductService(productStockBuilder))
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
        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(ImmutableMap.<ComponentReference, Object>builder().build());
        class ClassThatCannotExists {

        }

        // expected exception
        expectedException.expect(ApplicationException.class);
        expectedException
            .expectMessage(containsString("Dependency injection failure because " + ClassThatCannotExists.class.getName() + " was not found"));

        // when
        dependencyInjection.getBean(ClassThatCannotExists.class);
    }

    @Test
    public void shouldThrowAnErrorWhenImplementationIsNotInstanceOfClass() {
        // given
        final Map<ComponentReference, Object> map = ImmutableMap.<ComponentReference, Object>builder()
            .put(new ComponentReferenceDefault(ProductService.class), new Object())
            .build();
        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);

        // expected exception
        expectedException.expect(ApplicationException.class);
        expectedException
            .expectMessage(containsString(
                "Dependency injection failure because " + Object.class.getName() + " is not instance of " + ProductService.class.getName()));

        // when
        dependencyInjection.getBean(ProductService.class);
    }

}
