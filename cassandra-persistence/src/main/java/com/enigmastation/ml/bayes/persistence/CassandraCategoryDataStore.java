package com.enigmastation.ml.bayes.persistence;

import java.util.Map;

public class CassandraCategoryDataStore
extends CassandraDataStoreBase<String, Integer> {
  @Override
  protected void persistEntry(Map.Entry<String, Integer> pair) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Integer getDefault(Object k) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
