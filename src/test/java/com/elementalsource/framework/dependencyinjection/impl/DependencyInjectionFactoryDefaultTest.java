package com.elementalsource.framework.dependencyinjection.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.annotation.Component;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import org.junit.Test;
import org.reflections.Reflections;

public class DependencyInjectionFactoryDefaultTest {

    @Test
    public void shouldHaveAllComponents() {
        // given
        final Integer countOfComponents = new Reflections().getTypesAnnotatedWith(Component.class).size();
        final DependencyInjectionFactory dependencyInjectionFactory = DependencyInjectionFactoryDefault.getInstance();

        // when
        final DependencyInjection dependencyInjection = dependencyInjectionFactory.create();

        // then
        assertNotNull(dependencyInjection);
        assertEquals(countOfComponents, dependencyInjection.getBeansQuantity());
    }

}
