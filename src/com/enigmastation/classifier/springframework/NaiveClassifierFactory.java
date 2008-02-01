package com.enigmastation.classifier.springframework;

import org.springframework.beans.factory.FactoryBean;
import com.enigmastation.classifier.NaiveClassifier;
import com.enigmastation.classifier.impl.NaiveClassifierImpl;

public class NaiveClassifierFactory implements FactoryBean {
    public Object getObject() throws Exception {
        return new NaiveClassifierImpl();
    }

    public Class getObjectType() {
        return NaiveClassifier.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
