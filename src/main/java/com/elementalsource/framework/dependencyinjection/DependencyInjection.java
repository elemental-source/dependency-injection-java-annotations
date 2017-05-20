package com.elementalsource.framework.dependencyinjection;

public interface DependencyInjection {

    <T> T getBean(final Class<T> classBean);

    Integer getBeansQuantity();

}
