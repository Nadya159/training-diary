package com.nadya159.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<K, E> {

    Optional<E> findById(K id);

    List<E> findAll();

    void save(E e);
}
