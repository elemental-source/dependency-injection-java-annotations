package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;

public class DependencyInjectionDefault implements DependencyInjection {

    private final Map<Class<?>, Object> map;

    public DependencyInjectionDefault(final Map<Class<?>, Object> map) {
        this.map = ImmutableMap.copyOf(map);
    }
    public DependencyInjectionDefault() {
        this(Collections.emptyMap());
    }

    public <T> T getBean(Class<T> classBean) {
        final Object object = map.get(classBean);

        if (object == null) {
            throw new ApplicationException("Dependency injection failure because " + classBean.getName() + " was not found");
        }

        if (!classBean.isInstance(object)) {
            throw new ApplicationException("Dependency injection failure because " + object.getClass().getName() + " is not instance of " + classBean.getName());
        }

        return (T) object;
    }

    @Override
    public Integer getBeansQuantity() {
        return map.size();
    }
}
