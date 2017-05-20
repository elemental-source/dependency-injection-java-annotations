package com.elementalsource.framework.dependencyinjection;

import java.util.Set;

public interface DependencyInjectionFactory {

    DependencyInjection create(final Set<Class<?>> components);

}
