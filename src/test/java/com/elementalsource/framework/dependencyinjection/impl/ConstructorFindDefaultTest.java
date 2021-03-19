package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.ConstructorFind;
import com.elementalsource.framework.dependencyinjection.Inject;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Constructor;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConstructorFindDefaultTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ConstructorFind constructorFind = ConstructorFindDefault.getInstance();

    @Test
    public void shouldFindWhenDontHaveConstructor() {
        // given
        class MyComponent {
        }

        // when
        final Constructor<?> constructor = constructorFind.find(MyComponent.class);

        // then
        assertNotNull("explicacao", constructor);
        assertEquals(1, constructor.getParameterCount());
    }

    @Test
    public void shouldFindWhenHaveJustDefaultConstructor() {
        // given
        class MyComponent {
            MyComponent() {
            }
        }

        // when
        final Constructor<?> constructor = constructorFind.find(MyComponent.class);

        // then
        assertNotNull(constructor);
        assertEquals(1, constructor.getParameterCount());
    }

    @Test
    public void shouldFindWhenHaveConstrutorWithInjectAnnotation() {
        // given
        class MyComponent {
            @Inject
            MyComponent() {
            }
        }

        // when
        final Constructor<?> constructor = constructorFind.find(MyComponent.class);

        // then
        assertNotNull(constructor);
        assertEquals(1, constructor.getParameterCount());
    }

    @Test
    public void shouldFindWhenHaveConstrutorWithInjectAnnotationAndAnothersConstructors() {
        // given
        class MyComponent {
            @Inject
            MyComponent() {
            }

            MyComponent(String param1, Integer param2) {
            }
        }

        // when
        final Constructor<?> constructor = constructorFind.find(MyComponent.class);

        // then
        assertNotNull(constructor);
        assertEquals(1, constructor.getParameterTypes().length);
    }

    @Test
    public void shouldFindWhenHaveConstrutorWithParamsWithInjectAnnotationAndAnothersConstructors() {
        // given
        class MyComponent {
            @Inject
            MyComponent(Integer param1, String param2) {
            }

            MyComponent(String param1) {
            }
        }

        // when
        final Constructor<?> constructor = constructorFind.find(MyComponent.class);

        // then
        assertNotNull(constructor);
        assertEquals(3, constructor.getParameterCount());
    }

    @Test
    public void shouldNotFindWhenDontHaveConstrutor() {
        // given
        class MyComponent {
            private MyComponent() {
            }
        }

        // expected exception
        expectedException.expect(ApplicationException.class);
        expectedException
                .expectMessage(containsString(
                        "Could not be determined constructor on " + MyComponent.class.getName() + " because do not exist constructor"));

        // when
        constructorFind.find(MyComponent.class);
    }

    @Test
    public void shouldNotFindWhenHaveMoreThanOneConstrutor() {
        // given
        class MyComponent {
            MyComponent() {
            }

            MyComponent(String param1) {
            }
        }

        // expected exception
        expectedException.expect(ApplicationException.class);
        expectedException
                .expectMessage(containsString(
                        "Could not be determined constructor on " + MyComponent.class.getName() + " because have more than one constructor and any @Inject"));

        // when
        constructorFind.find(MyComponent.class);
    }

    @Test
    public void shouldNotFindWhenHaveMoreThanOneInjectAnnotation() {
        // given
        class MyComponent {
            @Inject
            MyComponent() {
            }

            @Inject
            MyComponent(String param1) {
            }
        }

        // expected exception
        expectedException.expect(ApplicationException.class);
        expectedException
                .expectMessage(containsString(
                        "Could not be determined constructor on " + MyComponent.class.getName() + " because have more than one constructor with @Inject"));

        // when
        constructorFind.find(MyComponent.class);
    }

}
