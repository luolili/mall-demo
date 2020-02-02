package com.mall.common.builder;

@FunctionalInterface
public interface MethodConsumer<T, V> {
    void accept(T method, V value);
}
