package com.elementalsource.framework.dependencyinjection;

import java.lang.reflect.Constructor;

public interface ConstructorFind {

    Constructor<?> find(final Class<?> classBean);

}
