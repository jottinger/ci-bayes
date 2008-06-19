package com.enigmastation.classifier.spring;

import com.enigmastation.classifier.CategoryIncrement;
import com.enigmastation.classifier.Classifier;
import com.enigmastation.classifier.ClassifierListener;
import com.enigmastation.classifier.FeatureIncrement;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class SpringTest {
    ApplicationContext ctx;
    Classifier c;

    @BeforeTest
    public void setup() {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("/spring.xml");
        c= (Classifier) ctx.getBean("classifier");
    }

    @AfterTest
    public void tearDown() {
        c=null;
    }
    
    @Test
    public void testCatCount() {
        assert c.getCategories().size()==0;
        c.train("this is but a test!", "test");
        assert c.getCategories().size()==1;
        c.getWeightedProbability("test", "test");
    }

    @Test
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
        c.train("the dog hopped over the log", "dog");
        c.train("a brown dog has fleas", "dog");
        c.train("a rotten dog is a spotted mule", "dog");
        c.train("the cat slept in the couch", "cat");
        c.train("the horse ran and jumped over the log", "horse");
        System.out.println(fucount[0]+","+cucount[0]);
        assert fucount[0]==24;
        assert cucount[0]==5;
        assert c.getWeightedProbability("the log was rotten", "dog")==0.5;
        assert c.getWeightedProbability("The log was rotten", "horse")==0.5;
        assert c.getWeightedProbability("The log was rotten", "cat")==0.5;
    }
}
