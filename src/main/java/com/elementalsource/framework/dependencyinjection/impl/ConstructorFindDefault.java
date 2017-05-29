package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.ConstructorFind;
import com.elementalsource.framework.dependencyinjection.Inject;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructorFindDefault implements ConstructorFind {
    private final static ConstructorFind INSTANCE = new ConstructorFindDefault();

    public static ConstructorFind getInstance() {
        return INSTANCE;
    }

    private ConstructorFindDefault() {
    }

    @Override
    public Constructor<?> find(final Class<?> classBean) {
        final Constructor<?>[] declaredConstructors = classBean.getDeclaredConstructors();

        if (declaredConstructors.length == 0) {
            throw new ApplicationException("Could not be determined constructor on " + classBean.getName() + " because do not exist constructor");
        } else if (declaredConstructors.length == 1) {
            return declaredConstructors[0];
        } else {
            final List<Constructor<?>> constructorsWithInject = Arrays
                    .stream(declaredConstructors)
                    .filter(constructor -> constructor.getDeclaredAnnotation(Inject.class) != null)
                    .collect(Collectors.toList());

            if (constructorsWithInject.isEmpty()) {
                throw new ApplicationException("Could not be determined constructor on " + classBean.getName() + " because have more than one constructor and any @Inject");
            } else if (constructorsWithInject.size() > 1) {
                throw new ApplicationException("Could not be determined constructor on " + classBean.getName() + " because have more than one constructor with @Inject");
            } else {
                return constructorsWithInject.get(0);
            }
        }
    }

}
