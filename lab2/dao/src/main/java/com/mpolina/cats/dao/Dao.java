package com.mpolina.cats.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    T findById(Long id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(T t);
}
