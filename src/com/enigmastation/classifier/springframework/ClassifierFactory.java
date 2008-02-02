package com.enigmastation.classifier.springframework;

import com.enigmastation.classifier.Classifier;
import com.enigmastation.classifier.impl.ClassifierImpl;
import org.springframework.beans.factory.FactoryBean;

public final class ClassifierFactory implements FactoryBean {
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
