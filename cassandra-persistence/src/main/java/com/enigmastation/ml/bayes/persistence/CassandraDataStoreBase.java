package com.enigmastation.ml.bayes.persistence;

import com.enigmastation.ml.common.collections.MapDataStore;
import com.enigmastation.ml.common.collections.ValueProvider;
import me.prettyprint.cassandra.serializers.DynamicCompositeSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.DynamicComposite;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

public abstract class CassandraDataStoreBase<K, V>
extends MapDataStore<K, V>
implements ValueProvider<K, V> {
  String hostName = "127.0.0.1";
  Integer port = 9160;
  String clusterName = "Test Cluster";
  String keySpaceName = "Test KeySpace";
  String columnFamilyName = "ColumnFamily";
  Cluster cluster = null;
  Keyspace keySpace = null;
  StringSerializer stringSerializer = StringSerializer.get();
  LongSerializer longSerializer = LongSerializer.get();
  ColumnFamilyTemplate<String, DynamicComposite> compTemplate;

  protected synchronized void connect() {
    if (cluster == null) {
      cluster = HFactory.getOrCreateCluster(getClusterName(), getHostName() + ":" + port);
      keySpace = HFactory.createKeyspace(getKeySpaceName(), cluster);
      compTemplate = new ThriftColumnFamilyTemplate<String, DynamicComposite>(keySpace,
                                                                             columnFamilyName,
                                                                             StringSerializer.get(),
                                                                             new DynamicCompositeSerializer());
    }
  }


  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getKeySpaceName() {
    return keySpaceName;
  }

  public void setKeySpaceName(String keySpaceName) {
    this.keySpaceName = keySpaceName;
  }

}
