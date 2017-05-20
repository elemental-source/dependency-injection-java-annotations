package com.elementalsource.framework.dependencyinjection.impl;

import static org.junit.Assert.assertEquals;

import com.elementalsource.framework.dependencyinjection.ComponentRepository;
import java.util.Set;
import org.junit.Test;

public class ComponentRepositoryDefaultTest {

    @Test
    public void shouldGenerateComponents() {
        // given
        final ComponentRepository instance = ComponentRepositoryDefault.getInstance();

        // when
        final Set<Class<?>> allComponents = instance.getAllComponents();

        // then
        assertEquals(6, allComponents.size());
    }

}
