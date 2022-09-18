package com.edu.ulab.app.storage;

import java.util.List;

public interface CrudMemoryRepository<T> extends MemoryStorage<T> {

    T save(T entity);

    T update(T entity);

    T findById(Long id);

    List<T> findAll();

    void deleteById(Long id);
}
