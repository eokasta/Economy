package com.github.eokasta.economy.dao.provider;

import java.util.List;
import java.util.Optional;

public interface Dao<T, T2> {

    Optional<T2> get(int id);

    Optional<T2> get(T t);

    boolean has(T t);

    List<T2> getAll();

    void save(T2 t);

    void delete(T2 t);
}