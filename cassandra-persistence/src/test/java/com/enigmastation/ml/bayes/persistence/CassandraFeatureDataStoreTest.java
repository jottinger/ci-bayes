package com.enigmastation.ml.bayes.persistence;

import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DynamicCompositeSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.DynamicComposite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import org.testng.annotations.Test;

public class CassandraFeatureDataStoreTest {

  @Test
  public void testConnectToCassandra() {
    Cluster cluster = HFactory.getOrCreateCluster("Test Cluster", "localhost");
    Keyspace keyspace = HFactory.createKeyspace("bayes", cluster);
    System.out.println(keyspace);
    ThriftColumnFamilyTemplate<String, DynamicComposite> compTemplate;

    compTemplate = new ThriftColumnFamilyTemplate<String, DynamicComposite>(keyspace,
                                                                           "feature",
                                                                           StringSerializer.get(),
                                                                           new DynamicCompositeSerializer());

    // how to add to the feature column family?
    ColumnFamilyUpdater<String, DynamicComposite> updater = compTemplate
                                                            .createUpdater("m"); //get Key and create updater object
    DynamicComposite composite = new DynamicComposite();
    composite.addComponent("monei", StringSerializer.get());
    composite.addComponent("good", StringSerializer.get());
  }
}
