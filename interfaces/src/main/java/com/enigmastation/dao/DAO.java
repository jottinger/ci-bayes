package com.enigmastation.dao;

import java.util.List;

public interface DAO<T extends BaseEntity<?>> {
    T read(T template);
    T write(T object);
    T take(T template);

    List<T> readMultiple(T template);
    List<T> takeMultiple(T template);
    T build();
}
