package com.enigmastation.resolvers.impl;

import com.enigmastation.resolvers.Resolver;
import com.enigmastation.resolvers.ResolverFactory;

public class MemoryResolverFactory implements ResolverFactory {
    public Resolver build() {
        return new MemoryResolver();
    }
}
