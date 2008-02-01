package springtests;

import com.enigmastation.classifier.CategoryIncrement;
import com.enigmastation.classifier.Classifier;
import com.enigmastation.classifier.ClassifierListener;
import com.enigmastation.classifier.FeatureIncrement;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {
    public static void main(String[] args) {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("spring.xml");
        Classifier c= (Classifier) ctx.getBean("classifier");
        final String user="foobaricus";
        c.addListener(new ClassifierListener() {
            public void handleFeatureUpdate(FeatureIncrement fi) {
                System.out.println(user+":"+fi);
            }

            public void handleCategoryUpdate(CategoryIncrement fi) {
                System.out.println(user+":"+fi);
            }
        });
        c.train("the dog hopped over the log", "dog");
        c.train("a brown dog has fleas", "dog");
        c.train("a rotten dog is a spotted mule", "dog");
        c.train("the cat slept in the couch", "cat");
        c.train("the horse ran and jumped over the log", "horse");
        System.out.println(c.getWeightedProbability("dog", "the log was rotten"));
        System.out.println(c.getWeightedProbability("horse", "The log was rotten"));
        System.out.println(c.getWeightedProbability("cat", "The log was rotten"));
    }
}
