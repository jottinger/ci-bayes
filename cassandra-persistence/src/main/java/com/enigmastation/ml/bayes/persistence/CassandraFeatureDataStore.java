package com.enigmastation.ml.bayes.persistence;

import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.AbstractComposite;
import me.prettyprint.hector.api.beans.DynamicComposite;

import java.util.Map;

public class CassandraFeatureDataStore
extends CassandraDataStoreBase<String, Map<String, Integer>> {

  @Override
  protected void persistEntry(Map.Entry<String, Map<String, Integer>> pair) {
    connect();
    String key = pair.getKey().substring(0, 1);
    // we update the row denoted by 'key' with dynamic composites that represent 
    // pair.getKey()+":"+category+":"+category count

    ColumnFamilyUpdater<String, DynamicComposite> updater = compTemplate
                                                            .createUpdater(key); //get Key and create updater object

    for (Map.Entry<String, Integer> o : pair.getValue().entrySet()) {
      DynamicComposite value = new DynamicComposite();
      value.addComponent(0, pair.getKey(), stringSerializer, "AsciiType", AbstractComposite.ComponentEquality.EQUAL);
      value.addComponent(1, o.getKey(), stringSerializer, "AsciiType", AbstractComposite.ComponentEquality.EQUAL);
      value
      .addComponent(2, o.getValue(), IntegerSerializer.get(), "IntegerType", AbstractComposite.ComponentEquality.EQUAL);
      updater.update();
    }

  }

  @Override
  public Map<String, Integer> getDefault(Object k) {
    connect();
    String keyObject = k.toString();
    String key = keyObject.substring(0, 1);
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
