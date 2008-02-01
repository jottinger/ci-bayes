package com.enigmastation.classifier.springframework;

import org.springframework.beans.factory.FactoryBean;
import com.enigmastation.classifier.impl.ClassifierImpl;
import com.enigmastation.classifier.Classifier;

public class ClassifierFactory implements FactoryBean {
    public Object getObject() throws Exception {
        return new ClassifierImpl();
    }

    public Class getObjectType() {
        return Classifier.class;
    }

    public boolean isSingleton() {
        return false; 
    }
}
