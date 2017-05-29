package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.Component;
import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class DependencyInjectionFactoryDefaultTest {

    @Component
    protected static class Controller {

    }

    @Component
    protected static class Service {

    }

    @Component
    protected static class DAO {

    }

    @Component
    protected static class PersonDAO {
        private final DAO dao;

        public PersonDAO(DAO dao) {
            this.dao = dao;
        }
    }

    @Component
    protected static class ProductDAO {
        private final DAO dao;

        public ProductDAO(DAO dao) {
            this.dao = dao;
        }
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

    @Test
    public void shouldInitComponentsWithSomeDependency() {
        // given
        final Set<Class<?>> components = ImmutableSet.<Class<?>>builder()
                .add(PersonDAO.class, ProductDAO.class, DAO.class)
                .build();
        final Integer countOfComponents = components.size();
        final DependencyInjectionFactory dependencyInjectionFactory = DependencyInjectionFactoryDefault.getInstance();

        // when
        final DependencyInjection dependencyInjection = dependencyInjectionFactory.create(components);
        final DAO dao = dependencyInjection.getBean(DAO.class);
        final PersonDAO personDAO = dependencyInjection.getBean(PersonDAO.class);
        final ProductDAO productDAO = dependencyInjection.getBean(ProductDAO.class);

        // then
        assertNotNull(dependencyInjection);
        assertEquals(countOfComponents, dependencyInjection.getBeansQuantity());
        assertNotNull(dao);
        assertNotNull(personDAO);
        assertNotNull(personDAO.dao);
        assertNotNull(productDAO);
        assertNotNull(productDAO.dao);
        assertSame(dao, personDAO.dao);
        assertSame(dao, productDAO.dao);
    }

}
