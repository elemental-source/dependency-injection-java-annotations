package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.ComponentReference;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class ComponentReferenceDefault implements ComponentReference {

    private final Set<Class<?>> references;

    public ComponentReferenceDefault(final Class<?> beanClass) {
        this.references = ImmutableSet.<Class<?>>builder()
            .add(beanClass)
            .add(beanClass.getInterfaces())
            .build();
    }

    @Override
    public Boolean contains(final Class<?> componentClass) {
        return references.contains(componentClass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComponentReferenceDefault)) {
            return false;
        }
        ComponentReferenceDefault that = (ComponentReferenceDefault) o;
        return that.references.stream().filter(this::contains).map(c -> true).findAny().orElse(false);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
