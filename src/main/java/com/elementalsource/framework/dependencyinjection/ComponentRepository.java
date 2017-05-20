package com.elementalsource.framework.dependencyinjection;

import java.util.Set;

public interface ComponentRepository {

    Set<Class<?>> getAllComponents();

}
