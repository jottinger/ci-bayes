package com.enigmastation.classifier.spring;

import com.enigmastation.classifier.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import static org.testng.Assert.*;

public class SpringTest {
    ApplicationContext ctx;
    Classifier c;

    @BeforeTest(groups = {"fulltest", "normal"})
    public void setup() {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("/spring.xml");
        c= (Classifier) ctx.getBean("classifier");
    }

    @AfterTest(groups={"fulltest", "normal"})
    public void tearDown() {
        c=null;
    }
    
    @Test(groups={"fulltest", "normal"})
    public void testCatCount() {
        assertEquals(c.getCategories().size(),0);
        ((Trainer)c).train("this is but a test!", "test");
        assertEquals(c.getCategories().size(),1);
        c.getWeightedProbability("test", "test");
    }

    @Test(groups={"fulltest", "normal"},dependsOnMethods = {"testCatCount"})
    public void testSpringFactories() {
        //final String user="foobaricus";
        final int[] fucount = new int[]{0};
        final int[] cucount = new int[]{0};
        c.addListener(new ClassifierListener() {
            public void handleFeatureUpdate(FeatureIncrement fi) {
                fucount[0]++;
                //System.out.println(user+":"+fi);
            }

            public void handleCategoryUpdate(CategoryIncrement fi) {
                cucount[0]++;
                //System.out.println(user+":"+fi);
            }
        });
        ((Trainer)c).train("the dog hopped over the log", "dog");
        ((Trainer)c).train("a brown dog has fleas", "dog");
        ((Trainer)c).train("a rotten dog is a spotted mule", "dog");
        ((Trainer)c).train("the cat slept in the couch", "cat");
        ((Trainer)c).train("the horse ran and jumped over the log", "horse");
        //System.out.println(fucount[0]+","+cucount[0]);
        assertEquals(fucount[0],24);
        assertEquals(cucount[0],5);
        assertEquals(c.getWeightedProbability("the log was rotten", "dog"),0.5);
        assertEquals(c.getWeightedProbability("The log was rotten", "horse"),0.5);
        assertEquals(c.getWeightedProbability("The log was rotten", "cat"),0.5);
    }
}
