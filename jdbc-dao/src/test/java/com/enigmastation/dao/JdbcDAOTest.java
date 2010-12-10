package com.enigmastation.dao;

import com.enigmastation.dao.model.Category;
import com.enigmastation.dao.model.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * User: joeo
 * Date: 12/5/10
 * Time: 8:31 AM
 * <p/>
 * Copyright
 */
@ContextConfiguration(locations="/jdbc-context.xml")
public class JdbcDAOTest extends AbstractTestNGSpringContextTests {
    @Autowired
    FeatureDAO featureDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @Test
    public void testConfig() {
        assertNotNull(featureDAO);
    }

    @BeforeMethod
    public void clearDB() {
        featureDAO.takeMultiple(new Feature());
    }

    @Test
    public void testFeatureDAO() {
        Feature f=new Feature();
        f.setCategory("bar");
        f.setFeature("foo");
        f.setCount(0L);
        featureDAO.write(f);
        System.out.println(f);
        Feature t=new Feature();
        t.setCategory("bar");
        List<Feature> l=featureDAO.readMultiple(t);
        assertEquals(l.size(), 1);
        featureDAO.takeMultiple(t);
        l=featureDAO.readMultiple(t);
        assertEquals(l.size(), 0);
    }
    @Test
    public void testCategoryDAO() {
        Category f=new Category();
        f.setCategory("bar");

        f.setCount(0L);
        categoryDAO.write(f);
        System.out.println(f);
        Category t=new Category();
        t.setCategory("bar");
        List<Category> l=categoryDAO.readMultiple(t);
        assertEquals(l.size(), 1);
        categoryDAO.takeMultiple(t);
        l=categoryDAO.readMultiple(t);
        assertEquals(l.size(), 0);
    }
}
