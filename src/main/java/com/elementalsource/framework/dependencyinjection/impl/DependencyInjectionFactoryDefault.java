package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import com.elementalsource.framework.dependencyinjection.annotation.Component;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.reflections.Reflections;

public class DependencyInjectionFactoryDefault implements DependencyInjectionFactory {

    private static final DependencyInjectionFactoryDefault DEPENDENCY_INJECTION_FACTORY_DEFAULT = new DependencyInjectionFactoryDefault();

    public static DependencyInjectionFactory getInstance() {
        return DEPENDENCY_INJECTION_FACTORY_DEFAULT;
    }

    private DependencyInjectionFactoryDefault() {
    }

    /**
     * Create bean
     * when have parameters, I try to create dependencies
     * if has a circular reference, I throw an exception
     *
     * @return Default DependencyInjection of System
     */
    public DependencyInjection create() {
        return new Reflections()
            .getTypesAnnotatedWith(Component.class)
            .stream()
            .findAny()
            .map(classBean -> {
                final HashMap<Class<?>, Object> constructedClasses = new HashMap<>();
                createBean(constructedClasses, classBean);
                return new DependencyInjectionDefault(constructedClasses);
            })
            .orElse(new DependencyInjectionDefault());
    }

    private Object createBean(final Map<Class<?>, Object> constructedClasses, final Class<?> classBean) {
        if (constructedClasses.containsKey(classBean)) {
            throw new ApplicationException("Circular reference on create bean " + classBean.getName());
        }
        final Constructor<?>[] constructors = classBean.getConstructors();
        try {
            if (constructors.length > 1) {
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
                constructedClasses.put(classBean, bean);
                return bean;
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ApplicationException("Error on create bean " + classBean.getName(), e);
        }
    }

}
