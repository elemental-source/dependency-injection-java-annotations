package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.ComponentReference;
import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DependencyInjectionFactoryDefault implements DependencyInjectionFactory {

    private static final DependencyInjectionFactoryDefault DEPENDENCY_INJECTION_FACTORY_DEFAULT = new DependencyInjectionFactoryDefault();

    public static DependencyInjectionFactory getInstance() {
        return DEPENDENCY_INJECTION_FACTORY_DEFAULT;
    }

    private DependencyInjectionFactoryDefault() {
    }

    public DependencyInjection create(final Set<Class<?>> components) {
        final HashMap<ComponentReference, Object> constructedClasses = new HashMap<>();

        components.forEach(classBean -> {
            if (!constructedClasses.containsKey(new ComponentReferenceDefault(classBean))) {
                createBean(constructedClasses, classBean);
            }
        });

        return new DependencyInjectionDefault(constructedClasses);
    }

    private Object createBean(final Map<ComponentReference, Object> constructedClasses, final Class<?> classBean) {
        final Constructor<?>[] constructors = classBean.getDeclaredConstructors();
        try {
            if (constructors.length > 1) {
                // TODO to implement annotation @Inject
                throw new ApplicationException("It is necessary have just one constructor with all dependencies on bean: " + classBean.getName());
            } else {
                // TODO apply lambda on this mess
                final Constructor<?> constructor = constructors[0];
                final List<Object> parameters = new ArrayList<>();
                for (Class<?> parameterType : constructor.getParameterTypes()) {
                    final ComponentReference componentReference = new ComponentReferenceDefault(parameterType);
                    final Object beanParamOfConstructor;
                    if (constructedClasses.containsKey(componentReference)) {
                        beanParamOfConstructor = constructedClasses.get(componentReference);
                    } else {
                        beanParamOfConstructor = createBean(constructedClasses, parameterType);
                    }
                    parameters.add(beanParamOfConstructor);
                }
                final Object bean = parameters.isEmpty() ? constructor.newInstance() : constructor.newInstance(parameters.toArray());

                return insertBean(constructedClasses, classBean, bean);
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ApplicationException("Error on create bean " + classBean.getName(), e);
        }
    }

    private Object insertBean(final Map<ComponentReference, Object> constructedClasses, final Class<?> classBean, final Object bean) {
        if (constructedClasses.containsKey(classBean)) {
            throw new ApplicationException("Circular reference on create bean " + classBean.getName());
        }

        constructedClasses.put(new ComponentReferenceDefault(classBean), bean);
        return bean;
    }

}
