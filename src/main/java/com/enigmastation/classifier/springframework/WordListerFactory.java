package com.enigmastation.classifier.springframework;

import com.enigmastation.classifier.WordLister;
import com.enigmastation.classifier.impl.SimpleWordLister;
import org.springframework.beans.factory.FactoryBean;

public final class WordListerFactory implements FactoryBean {
    public Object getObject() throws Exception {
        return new SimpleWordLister();
    }

    public Class getObjectType() {
        return WordLister.class;
    }

    public boolean isSingleton() {
        return false;  
    }
}
