package com.enigmastation.ml.bayes.persistence;

import com.enigmastation.ml.common.collections.MapDataStore;
import com.enigmastation.ml.common.collections.ValueProvider;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import java.util.Arrays;
import java.util.Map;

public class CassandraDataStore extends MapDataStore implements ValueProvider {
    private Cluster cluster;
    private Integer port = 9160;
    private String clusterName = "Test Cluster";
    private String host = "localhost";
    private String keyspaceName = "keySpace";
    private Keyspace keyspace;
    private final static StringSerializer stringSerializer = StringSerializer.get();
    private String columnFamily="columnFamily";
    Mutator mutator;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getKeyspaceName() {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void connect() throws Exception {
        cluster = HFactory.getOrCreateCluster(clusterName, host + ":" + port);
        KeyspaceDefinition keyspaceDefinition = cluster.describeKeyspace(keyspaceName);
        if (keyspaceDefinition == null) {
            createSchema(cluster);
        }
        keyspace = HFactory.createKeyspace(keyspaceName, cluster);
    }

    private void createSchema(Cluster cluster) {
        ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(keyspaceName, columnFamily,
                ComparatorType.BYTESTYPE);
        KeyspaceDefinition ksDef = HFactory.createKeyspaceDefinition(keyspaceName,
                ThriftKsDef.DEF_STRATEGY_CLASS,
                1,
                Arrays.asList(cfDef));
        cluster.addKeyspace(ksDef);
    }

    @Override
    protected void endBlock() {
        super.endBlock();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Keyspace getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(Keyspace keyspace) {
        this.keyspace = keyspace;
    }

    @Override
    protected void startBlock() {
        mutator=HFactory.createMutator(getKeyspace(), stringSerializer);
    }

    @Override
    protected void persistEntry(Map.Entry pair) {
        String type = "simple";
        // in a pair, examine the value; if it's a map, translate each entry into a column
        // the column key is the pair's key
        // a type indicator is used to indicate the actual type ("complex" for map or "simple" for single entry)
        if (Map.class.isAssignableFrom(pair.getValue().getClass())) {
            // we have a map. Iterate through the keys and generate a row from them.
            type = "complex";

        } else {

        }
    }

    @Override
    public Object getDefault(Object k) {
        // load a row with key k
        // if the row has a type of "feature" then create a map and return that, otherwise it's a simple object and is returned as such
        return null;
    }
}
