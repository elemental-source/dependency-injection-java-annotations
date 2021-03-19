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
import org.mockito.Mockito;

import java.util.Map;

//import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInjectionDefaultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldTakeRightImplementation() {
        // given
        Map<ComponentReference, Object> map = mock(Map.class);
        ProductService productService = mock(ProductService.class);

        when(map.get(any())).thenReturn(productService);

        final DependencyInjection subject = new DependencyInjectionDefault(map);

        // when
        final ProductService result = subject.getBean(ProductService.class);

        // then
        assertNotNull(result);
        assertTrue(result instanceof ProductService);
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
        Map<ComponentReference, Object> map = mock(Map.class);
        Object o = mock(Object.class);
        when(map.get(any())).thenReturn(o);
        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);
        class ClassThatCannotExists {

        }

        // expected exception
        expectedException.expect(ApplicationException.class);

        // when
        dependencyInjection.getBean(ClassThatCannotExists.class);
    }

    @Test
    public void shouldThrowAnErrorWhenImplementationIsNotInstanceOfClass() {
        // given
        Map<ComponentReference, Object> map = mock(Map.class);
        Object o = mock(Object.class);
        when(map.get(any())).thenReturn(o);

        final DependencyInjection dependencyInjection = new DependencyInjectionDefault(map);

//        // expected exception
        expectedException.expect(ApplicationException.class);

        // when
        dependencyInjection.getBean(ProductService.class);
    }

}
