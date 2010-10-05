package com.enigmastation.classifier.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.enigmastation.classifier.ClassifierDataModelFactory;

public class BasicClassifierDataModelFactory implements ClassifierDataModelFactory
{
    protected Map<String, Integer> categoryCountMap = new ConcurrentHashMap<String, Integer>();
    protected Map<String, Map<String,Integer>> featureMap = new ConcurrentHashMap<String, Map<String, Integer>>();
    protected String persistentFile;

    public String getPersistentFile()
    {
        return persistentFile;
    }

    public void setPersistentFile(String persistentFile)
    {
        this.persistentFile = persistentFile;
    }

    @SuppressWarnings("unchecked")
	public void init()
    {
        if(persistentFile != null && new File(persistentFile).exists())
        {

            FileInputStream fos = null;
            ObjectInputStream oos = null;
            try
            {
                fos = new FileInputStream(persistentFile);
                oos = new ObjectInputStream(fos);
                categoryCountMap = (Map<String, Integer>) oos.readObject();
                this.featureMap = (Map<String, Map<String, Integer>>) oos.readObject();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if(oos != null)
                        oos.close();

                }
                catch (Exception e)
                {

                }

                try
                {
                    if(fos != null)
                        fos.close();

                }
                catch (Exception e)
                {

                }
            }
        }
    }

    public void destroy()
    {
        if(persistentFile != null)
        {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try
            {
                fos = new FileOutputStream(persistentFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(categoryCountMap);
                oos.writeObject(featureMap);
                oos.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    oos.close();
                    fos.close();
                }
                catch (IOException e)
                {
                    
                }

            }
        }
    }

    public Map<String,Integer> getCategoryCountMap()
    {
        return categoryCountMap;
    }

    public Map<String, Integer> getFeatureMap(String feature)
    {
        Map<String, Integer> fm = featureMap.get(feature);
        if(fm == null)
        {
            fm = new ConcurrentHashMap<String, Integer>();
            featureMap.put(feature, fm);
        }
        return fm;
    }
}
