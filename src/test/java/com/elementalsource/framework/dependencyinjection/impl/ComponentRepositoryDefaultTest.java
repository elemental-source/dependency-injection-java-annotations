package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.ComponentRepository;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ComponentRepositoryDefaultTest {

    @Test
    public void shouldGenerateComponents() {
        // given
        final ComponentRepository instance = ComponentRepositoryDefault.getInstance();

        // when
        final Set<Class<?>> allComponents = instance.getAllComponents();

        // then
        assertEquals(9, allComponents.size());
    }

}
