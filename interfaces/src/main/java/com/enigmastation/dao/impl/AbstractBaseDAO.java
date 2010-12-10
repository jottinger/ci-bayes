package com.enigmastation.dao.impl;

import com.enigmastation.dao.BaseEntity;
import com.enigmastation.dao.DAO;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractBaseDAO<T extends BaseEntity> implements DAO<T> {
    protected Class<?> persistentClass = null;

    @SuppressWarnings({"unchecked"})
    protected AbstractBaseDAO() {
        persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

    }

    @SuppressWarnings({"unchecked"})
    public T build() {
        try {
            return (T) persistentClass.newInstance();
        } catch (InstantiationException e) {
            throw new Error("Could not instantiate instance of "+persistentClass, e);
        } catch (IllegalAccessException e) {
            throw new Error("Could not instantiate instance of "+persistentClass, e);
        }
    }
}
