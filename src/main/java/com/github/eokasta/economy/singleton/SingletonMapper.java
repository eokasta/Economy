package com.github.eokasta.economy.singleton;

import com.github.eokasta.economy.singleton.annotation.Singleton;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class SingletonMapper {

    private final static Map<String, Object> ELEMENTS = Maps.newConcurrentMap();

    @SneakyThrows
    public static <T> T of(Class<T> clazz, Object... objects) {
        final Singleton singleton = findSingleton(clazz);
        final Object object = ELEMENTS.get(singleton.id());

        if (object == null) {
            T instance = findInstance(clazz, objects);

            ELEMENTS.put(singleton.id(), instance);

            return instance;
        }

        return (T) object;
    }

    public static <T> Singleton findSingleton(Class<T> clazz) throws Exception {
        if (!clazz.isAnnotationPresent(Singleton.class))
            throw new Exception("Singleton annotation isn't on the class (" + clazz.getSimpleName() + ")");

        return clazz.getDeclaredAnnotation(Singleton.class);
    }

    public static <T> T findInstance(Class<T> clazz, Object... objects) throws Exception {
        try {
            final Constructor<T> constructor = (Constructor<T>) clazz.getConstructors()[0];
            constructor.setAccessible(true);

            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new Exception("Can't made a new instance from the class (" + clazz.getSimpleName() + ").");
        } catch (IllegalArgumentException exception) {
            throw new Exception("This class is not instantiated correctly.");
        }
    }

}
