package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.ComponentRepository;
import com.elementalsource.framework.dependencyinjection.Component;
import java.util.Set;
import org.reflections.Reflections;

public class ComponentRepositoryDefault implements ComponentRepository {

    private static final ComponentRepositoryDefault COMPONENT_REPOSITORY_DEFAULT = new ComponentRepositoryDefault();

    public static ComponentRepository getInstance() {
        return COMPONENT_REPOSITORY_DEFAULT;
    }

    private ComponentRepositoryDefault() {
    }

    @Override
    public Set<Class<?>> getAllComponents() {
        return new Reflections().getTypesAnnotatedWith(Component.class);
    }

}
