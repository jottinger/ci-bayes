package com.enigmastation.dao;

import java.util.List;

public interface DAO<T extends BaseEntity> {
    T read(T template);
    T readById(String id);
    T write(T object);
    T take(T template);
    T takeById(String id);

    List<T> readMultiple(T template);
    List<T> takeMultiple(T template);
    T build();
}
