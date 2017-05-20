package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyInjectionFactoryDefault implements DependencyInjectionFactory {

    private static final DependencyInjectionFactoryDefault DEPENDENCY_INJECTION_FACTORY_DEFAULT = new DependencyInjectionFactoryDefault();

    public static DependencyInjectionFactory getInstance() {
        return DEPENDENCY_INJECTION_FACTORY_DEFAULT;
    }

    private DependencyInjectionFactoryDefault() {
    }

    public DependencyInjection create(final Set<Class<?>> components) {
        final HashMap<Class<?>, Object> constructedClasses = new HashMap<>();

        components.forEach(classBean -> {
            if (!constructedClasses.containsKey(classBean)) {
                createBean(constructedClasses, classBean);
            }
        });
        return new DependencyInjectionDefault(constructedClasses);
    }

    private Object createBean(final Map<Class<?>, Object> constructedClasses, final Class<?> classBean) {
        final Constructor<?>[] constructors = classBean.getConstructors();
        try {
            if (constructors.length == 0) {
                final Object bean = classBean.newInstance();
                return insertBean(constructedClasses, classBean, bean);
            } else if (constructors.length > 1) {
                // TODO to implement annotation @Inject
                throw new ApplicationException("It is necessary have just one constructor with all dependencies on bean: " + classBean.getName());
            } else {
                // TODO apply lambda on this mess
                final Constructor<?> constructor = constructors[0];
                final List<Object> parameters = new ArrayList<>();
                for (Class<?> parameterType : constructor.getParameterTypes()) {
                    if (!constructedClasses.containsKey(parameterType)) {
                        parameters.add(createBean(constructedClasses, parameterType));
                    }
                }
                final Object bean = parameters.isEmpty() ? constructor.newInstance() : constructor.newInstance(parameters.toArray());

                return insertBean(constructedClasses, classBean, bean);
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ApplicationException("Error on create bean " + classBean.getName(), e);
        }
    }

    private Object insertBean(final Map<Class<?>, Object> constructedClasses, final Class<?> classBean, final Object bean) {
        if (constructedClasses.containsKey(classBean)) {
            throw new ApplicationException("Circular reference on create bean " + classBean.getName());
        }

        constructedClasses.put(classBean, bean);
        return bean;
    }

}
