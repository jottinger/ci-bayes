package com.enigmastation.dao.gigaspaces;

import com.enigmastation.classifier.Classifier;
import com.enigmastation.dao.CategoryDAO;
import com.enigmastation.dao.FeatureDAO;
import com.enigmastation.dao.model.Category;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * User: joeo
 * Date: 11/28/10
 * Time: 8:31 AM
 * <p/>
 * Copyright
 */
@ContextConfiguration(locations = {"classpath:/ci-bayes-context.xml", "classpath:/gstest.xml"})
public class GigaspacesTest extends AbstractTestNGSpringContextTests {
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    FeatureDAO featureDAO;
    @Autowired
    Classifier classifier;
    @Autowired
    GigaSpace space;

    @BeforeMethod
    public void clearSpace() {
        space.clean();
    }

    @Test
    public void testConfig() {
        assertNotNull(categoryDAO);
        assertNotNull(featureDAO);
        assertNotNull(classifier);
        System.out.println(classifier);
    }

    @Test
    public void testFindById() {
        Category c = new Category();
        c.setCategory("test");
        c.setCount(10L);
        categoryDAO.write(c);
        assertNotNull(c.getId());
        Category template = new Category();
        template.setId(c.getId());
        Category o = categoryDAO.read(template);
        assertNotNull(o);
        assertEquals(o.getId(), c.getId());
        assertEquals(o.getCategory(), c.getCategory());
    }

    @Test
    public void testCategoryDAO() {
        Category c = new Category();
        c.setCategory("test");
        c.setCount(10L);
        categoryDAO.write(c);
        Category t = new Category();
        t.setCategory("test");
        Category t2 = categoryDAO.read(t);
        assertEquals(t2.getCount(), c.getCount());
        assertEquals(t2.getId(), c.getId());
    }

    @Test
    public void testClassifierData() {
        classifier.train("this is a test", "test");
        classifier.train("this is not a test", "notest");
        dumpSpace();
    }

    private void dumpSpace() {
        Object[] objects = space.readMultiple(new Object(), Integer.MAX_VALUE);
        for (Object o : objects) {
            System.out.println(o);
        }
    }
}
