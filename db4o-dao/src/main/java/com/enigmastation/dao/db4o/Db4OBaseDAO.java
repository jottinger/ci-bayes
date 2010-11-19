package com.enigmastation.dao.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.enigmastation.dao.impl.AbstractBaseDAO;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class Db4OBaseDAO<T extends Db4OBaseEntity> extends AbstractBaseDAO<T> {
    String dbFilename = "dbFile";

    static ObjectContainer db;
    static ReentrantLock lock = new ReentrantLock();

    public Db4OBaseDAO() {
        lock.lock();
        if (db == null) {
            db = Db4oEmbedded.openFile(Db4oEmbedded
                    .newConfiguration(), dbFilename);
            System.out.println("opening " + db);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    lock.lock();
                    if (db != null) {
                        if (!db.ext().isClosed()) {
                            System.out.println("closing " + db);
                            db.close();
                        }
                        db = null;
                    }
                    lock.unlock();
                }
            });
        }
        lock.unlock();
    }

    public T read(T template) {
        ObjectSet<T> existingObjects = db.queryByExample(template);
        return existingObjects.next();
    }

    public T write(T object) {
        if (object.getId() == null) {
            object.setId(UUID.randomUUID().toString());
        } else {
            /*
            we already had an id. If this object is active, then all is well; if not, we need to find
            and remove the prior entry.
             */
            if (!db.ext().isActive(object)) {
                T template = build();
                template.setId(object.getId());
                ObjectSet<T> existingObjects = db.queryByExample(template);
                for (T foundObject : existingObjects) {
                    db.delete(foundObject);
                }
            }
        }
        if (object.getCreateTime() == null) {
            object.setCreateTime(System.nanoTime());
        }
        object.setLastUpdateTime(System.nanoTime());

        db.store(object);
        return object;
    }

    public T take(T template) {
        ObjectSet<T> existingObjects = db.queryByExample(template);
        if (existingObjects.hasNext()) {
            T returnVal = existingObjects.next();
            db.delete(returnVal);
            return returnVal;
        } else {
            return null;
        }
    }

    public List<T> readMultiple(T template) {
        return null;
    }

    public List<T> takeMultiple(T template) {
        return null;
    }
}
