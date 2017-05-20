package com.elementalsource.framework.dependencyinjection.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ComponentReferenceDefaultTest {

    @Test
    public void shouldBeEqual() {
        // given
        class Controller {

        }

        // when
        final ComponentReferenceDefault componentReferenceDefault = new ComponentReferenceDefault(Controller.class);

        // then
        assertEquals(new ComponentReferenceDefault(Controller.class), componentReferenceDefault);
    }

    private interface Service {

    }

    @Test
    public void shouldBeEqualWhenHaveInterfaceAndSearchForInterface() {
        // given
        class Controller implements Service {

        }

        // when
        final ComponentReferenceDefault componentReferenceDefault = new ComponentReferenceDefault(Controller.class);

        // then
        assertEquals(new ComponentReferenceDefault(Service.class), componentReferenceDefault);
    }

    @Test
    public void shouldBeEqualWhenHaveInterfaceAndSearchForImplementation() {
        // given
        class Controller implements Service {

        }

        // when
        final ComponentReferenceDefault componentReferenceDefault = new ComponentReferenceDefault(Controller.class);

        // then
        assertEquals(new ComponentReferenceDefault(Controller.class), componentReferenceDefault);
    }

}
