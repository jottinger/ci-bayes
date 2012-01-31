package com.enigmastation.ml.bayes.persistence;

import org.apache.cassandra.thrift.CassandraDaemon;

import java.io.File;
import java.io.IOException;

public class CassandraTestUtil {
    CassandraDaemon cassandraDaemon;

    public void startCassandra(String resource) throws IOException {
        File f = new File(resource);
        System.setProperty("cassandra.config", resource); //f.toURI().toURL().toExternalForm());
        cassandraDaemon = new CassandraDaemon();
        cassandraDaemon.init(null);
        cassandraDaemon.start();
    }

    public void shutDownCassandra() {
        cassandraDaemon.stop();
    }
}