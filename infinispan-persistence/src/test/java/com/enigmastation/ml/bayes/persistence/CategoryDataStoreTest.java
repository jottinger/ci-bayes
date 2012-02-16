package com.enigmastation.ml.bayes.persistence;

import com.enigmastation.ml.common.collections.MapWithDefaultValue;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CategoryDataStoreTest {
    @Test
    public void testCategoryDataStore() {
        InfinispanCategoryDataStore categoryDatastore = new InfinispanCategoryDataStore();
        categoryDatastore.setCacheName("category");
        MapWithDefaultValue<String, Integer> map = new MapWithDefaultValue<>(categoryDatastore);
        map.setDataStore(categoryDatastore);
        map.put("foo", 1);
        assertEquals(1, map.get("foo").intValue());
        assertEquals(0, map.get("bar").intValue());
    }
}
