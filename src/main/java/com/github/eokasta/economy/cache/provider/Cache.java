package com.github.eokasta.economy.cache.provider;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Cache<T, T2> {

    Optional<T2> get(T t);
    boolean has(T t);
    Collection<T> getKeys();
    Collection<T2> getValues();
    void put(T t, T2 t2);
    void remove(T t);

}
