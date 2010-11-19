package com.enigmastation.resolvers;

public interface Resolver {
    /**
     * returns true if the id requested is available for key
     *
     * @param id  the requested id
     * @param key the key for the id assigned
     * @return success if the key is available and assigned
     */
    boolean addKey(String key, int id);

    /**
     * Adds a key to the resolver, returning the id for the key. If the
     * key is already in the resolver, returns the originally-assigned
     * key.
     *
     * @param key The key to add
     * @return the id for the key
     */
    int addKey(String key);

    int getId(String key);

    String getKey(int id);

    int getIdForKey(String key);
}