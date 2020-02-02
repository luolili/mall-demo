package com.mall.common.builder;

import com.mall.common.vo.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 链式构建实体类
 */
public class EntityBuilder<T> {
    private final Supplier<T> entitySupplier;

    private List<Consumer<T>> methodList = new ArrayList<>();

    public EntityBuilder(Supplier<T> entitySupplier) {
        this.entitySupplier = entitySupplier;
    }

    public static <T> EntityBuilder<T> of(Supplier<T> entitySupplier) {
        return new EntityBuilder<>(entitySupplier);
    }

    public <V> EntityBuilder<T> with(MethodConsumer<T, V> method, V p1) {
        Consumer<T> c = instance -> method.accept(instance, p1);

        methodList.add(c);
        return this;
    }

    public T build() {
        T entity = entitySupplier.get();
        methodList.forEach(m -> m.accept(entity));
        methodList.clear();
        return entity;


    }

    public static void main(String[] args) {
        EntityBuilder<Person> p = EntityBuilder.of(Person::new).with(Person::setName, "df");
        Person person = p.build();

        System.out.println(person.getName());
    }
}
