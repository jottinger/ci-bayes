package com.enigmastation.classifier.persistence;

import com.enigmastation.classifier.Classifier;

import java.io.*;
import java.util.Map;

/**
 * This class uses serialization to store "pure" classifier data to a file. It doesn't preserve any
 * modifications like the NaiveClassifier's threshold data.
 * 
 * @version $Revision$
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 */
public class Serializer {
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    String filename="classifier.ser";

    @SuppressWarnings("unchecked")
    public void load(Classifier classifier) throws IOException, ClassNotFoundException {
        FileInputStream fos=new FileInputStream(filename);
        ObjectInputStream oos=new ObjectInputStream(fos);

        Map<String, Integer> cc= (Map<String, Integer>) oos.readObject();
        Map<String, Map<String, Integer>> fc= (Map<String, Map<String, Integer>>) oos.readObject();

        classifier.getCategoryDocCount().clear();
        classifier.getCategoryFeatureMap().clear();

        classifier.getCategoryDocCount().putAll(cc);
        classifier.getCategoryFeatureMap().putAll(fc);

        oos.close();
        fos.close();

    }
    
    public void save(Classifier classifier) throws IOException {
        FileOutputStream fos=new FileOutputStream(filename);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(classifier.getCategoryDocCount());
        oos.writeObject(classifier.getCategoryFeatureMap());
        oos.flush();
        oos.close();
        fos.close();
    }
}
