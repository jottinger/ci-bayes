package com.enigmastation.classifier.springframework;

import org.springframework.beans.factory.FactoryBean;
import com.enigmastation.classifier.impl.WordListerImpl;
import com.enigmastation.classifier.WordLister;

public class WordListerFactory implements FactoryBean {
    public Object getObject() throws Exception {
        return new WordListerImpl();
    }

    public Class getObjectType() {
        return WordLister.class;
    }

    public boolean isSingleton() {
        return false;  
    }
}
