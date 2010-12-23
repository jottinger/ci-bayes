package com.enigmastation.dao.gigaspaces;

import com.enigmastation.dao.BaseEntity;
import com.enigmastation.dao.impl.AbstractBaseDAO;
import net.jini.core.lease.Lease;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * User: joeo
 * Date: 11/25/10
 * Time: 8:36 AM
 * <p/>
 * Copyright
 */
public class GigaspacesBaseDAO<T extends BaseEntity> extends AbstractBaseDAO<T> {
    @Autowired
    GigaSpace space;

    protected boolean hasId(T entry) {
        return (entry.getId() != null);
    }

    public T read(T template) {
        if (hasId(template)) {
            return readById(template.getId());
        }
        return space.read(template);
    }

    @SuppressWarnings({"unchecked"})
    public T readById(String id) {
        return (T) space.readById(persistentClass, id);
    }

    public T write(T entry) {
        return write(entry, Lease.FOREVER);
    }

    public T write(T entry, long timeout) {
        if (hasId(entry)) {
            takeById(entry.getId());
        } else {
            entry.setId(UUID.randomUUID().toString());
            entry.setCreateTime(System.nanoTime());
        }
        entry.setLastUpdateTime(System.nanoTime());
        space.write(entry, timeout);
        return entry;
    }

    public T take(T template) {
        if (hasId(template)) {
            return takeById(template.getId());
        }
        return space.take(template);
    }

    @SuppressWarnings({"unchecked"})
    public T takeById(String id) {
        return (T) space.takeById(persistentClass, id);
    }

    public List<T> readMultiple(T template) {
        return Arrays.asList(space.readMultiple(template, Integer.MAX_VALUE));
    }

    public List<T> takeMultiple(T template) {
        return Arrays.asList(space.takeMultiple(template, Integer.MAX_VALUE));
    }
}
