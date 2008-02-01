package springtests;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.enigmastation.classifier.Classifier;

/**
 * Created by IntelliJ IDEA.
 * User: jottinger
 * Date: Feb 1, 2008
 * Time: 3:41:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpringTest {
    public static void main(String[] args) {
        ApplicationContext ctx=new ClassPathXmlApplicationContext("spring.xml");
        Object o=ctx.getBean("classifier");
        System.out.println(o.getClass().getName());
    }
}
