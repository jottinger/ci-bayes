package com.enigmastation.classifier.spring;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.enigmastation.classifier.Classifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@ContextConfiguration(locations = "/spring.xml")
public class SpringTest extends AbstractTestNGSpringContextTests {
    @Autowired
    Classifier c;

    @BeforeTest
    public void clearDB() {
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded
                .newConfiguration(), "dbFile");
        ObjectSet set = db.queryByExample(new Object());
        for (Object o : set) {
            System.out.println("deleting object " + o + " from database");
            db.delete(o);
        }
        db.close();
    }
    /*
    @BeforeTest(groups = {"fulltest", "normal"})
    public void setup() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
        c = (Classifier) ctx.getBean("classifier");
    }
    */

    /*
    @AfterTest(groups = {"fulltest", "normal"})
    public void tearDown() {
        c = null;
    }
    */

    @Test(groups = {"fulltest", "normal"})
    public void testCatCount() {
        assertEquals(c.getCategories().size(), 0);
        c.train("this is but a test!", "test");
        assertEquals(c.getCategories().size(), 1);
        c.getWeightedProbability("test", "test");
    }
}
