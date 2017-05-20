package com.elementalsource.framework.dependencyinjection.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import com.elementalsource.framework.dependencyinjection.annotation.Component;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

public class DependencyInjectionFactoryDefaultTest {

    @Component
    protected static class Controller {

    }

    @Component
    protected static class Service {

    }

    @Test
    public void shouldHaveAllComponents() {
        // given
        final Set<Class<?>> components = ImmutableSet.<Class<?>>builder()
            .add(Controller.class, Service.class)
            .build();
        final Integer countOfComponents = components.size();
        final DependencyInjectionFactory dependencyInjectionFactory = DependencyInjectionFactoryDefault.getInstance();

        // when
        final DependencyInjection dependencyInjection = dependencyInjectionFactory.create(components);

        // then
        assertNotNull(dependencyInjection);
        assertEquals(countOfComponents, dependencyInjection.getBeansQuantity());
    }

    @Test
    public void shouldDoNotHaveComponents() {
        // given
        final Set<Class<?>> components = Collections.emptySet();
        final Integer countOfComponents = components.size();
        final DependencyInjectionFactory dependencyInjectionFactory = DependencyInjectionFactoryDefault.getInstance();

        // when
        final DependencyInjection dependencyInjection = dependencyInjectionFactory.create(components);

        // then
        assertNotNull(dependencyInjection);
        assertEquals(countOfComponents, dependencyInjection.getBeansQuantity());
    }

}
