package com.enigmastation.ml.bayes.persistence;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class CassandraPersistenceTest {
    CassandraTestUtil cassandraUtil = new CassandraTestUtil();
    private FileUtil fileUtil = new FileUtil();

    @BeforeClass
    public void startCassandra() throws IOException {
        final String ref[] = new String[1];
        FileCallback fileCallback = new FileCallback() {
            @Override
            public void found(String filename) {
                ref[0] = filename;
            }
        };
        fileUtil.findFile("cassandra\\.yaml", fileCallback);
        System.out.println(ref[0]);
        ref[0] = ref[0].substring(!ref[0].contains(":") ? 0 : ref[0].indexOf(":") + 1);
        cassandraUtil.startCassandra("file:" + ref[0]);
    }

    @AfterClass
    public void shutdownCassandra() {
        cassandraUtil.shutDownCassandra();
    }

    @Test
    public void testCassandraPersistence() {
    }
}
