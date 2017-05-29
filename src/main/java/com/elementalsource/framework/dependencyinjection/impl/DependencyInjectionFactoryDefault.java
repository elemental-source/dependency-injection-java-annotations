package com.elementalsource.framework.dependencyinjection.impl;

import com.elementalsource.framework.dependencyinjection.ComponentReference;
import com.elementalsource.framework.dependencyinjection.ConstructorFind;
import com.elementalsource.framework.dependencyinjection.DependencyInjection;
import com.elementalsource.framework.dependencyinjection.DependencyInjectionFactory;
import com.elementalsource.framework.dependencyinjection.infra.exception.ApplicationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DependencyInjectionFactoryDefault implements DependencyInjectionFactory {

    private static final DependencyInjectionFactoryDefault INSTANCE = new DependencyInjectionFactoryDefault(ConstructorFindDefault.getInstance());

    public static DependencyInjectionFactory getInstance() {
        return INSTANCE;
    }

    private final ConstructorFind constructorFind;

    private DependencyInjectionFactoryDefault(final ConstructorFind constructorFind) {
        this.constructorFind = constructorFind;
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
        // TODO apply lambda on this mess
        final List<Object> parameters = new ArrayList<>();
        final Constructor<?> constructor = constructorFind.find(classBean);
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
        try {
            final Object bean = parameters.isEmpty() ? constructor.newInstance() : constructor.newInstance(parameters.toArray());

            return insertBean(constructedClasses, classBean, bean);
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
