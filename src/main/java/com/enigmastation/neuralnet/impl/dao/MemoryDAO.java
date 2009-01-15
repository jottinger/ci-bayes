package com.enigmastation.neuralnet.impl.dao;

import com.enigmastation.neuralnet.NeuralNetDAO;
import com.enigmastation.neuralnet.model.Linkage;
import com.enigmastation.neuralnet.model.Layer;
import com.enigmastation.neuralnet.impl.resolvers.BaseResolver;
import com.enigmastation.neuralnet.impl.BaseLinkage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;
import javolution.util.FastSet;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Dec 29, 2008
 * Time: 4:07:13 AM
 * <p/>
 * <p>This class is licensed under the Apache Software License, available at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
 * No guarantees are made for fitness of use for any purpose whatsoever, and no responsibility is assigned to
 * its author for the results of any use. Note section 7 of the ASL 2.0, please, and if someone dies because of
 * this class, I'm sorry, but it's not my fault. I warned you.
 */
public class MemoryDAO extends BaseResolver implements NeuralNetDAO {
    Map<Layer,Map<Integer, Map<Integer, Linkage>>> network=new FastMap<Layer, Map<Integer, Map<Integer, Linkage>>>();

    {
        network.put(Layer.FROMHIDDEN, new FastMap<Integer, Map<Integer, Linkage>>());
        network.put(Layer.TOHIDDEN, new FastMap<Integer, Map<Integer, Linkage>>());
    }
    Map<Integer, Map<Integer, Linkage>> getNetworkByLayer(Layer layer) {
        return network.get(layer);
    }
    Map<Integer, Linkage> getByOrigin(Layer layer, Integer origin) {
        return getNetworkByLayer(layer).get(origin);
    }
    public Linkage getLinkage(Layer layer, Integer origin, Integer dest) {
        Map<Integer, Linkage> toMap=getNetworkByLayer(layer).get(origin);
        if(toMap!=null) {
            return toMap.get(dest);
        }
        return null;
    }

    public void setStrength(Layer layer, Integer origin, Integer dest, double v) {
        Linkage linkage=getLinkage(layer, origin, dest);
        if(linkage==null) {
            linkage=new BaseLinkage(layer, origin, dest, v);
            Map<Integer, Linkage> map=getByOrigin(layer, origin);
            if(map==null) {
                map=new FastMap<Integer, Linkage>();
                getNetworkByLayer(layer).put(origin, map);
            }
            map.put(dest, linkage);
        } else {
            linkage.setStrength(v);
        }
    }

    public Collection<? extends Integer> getHiddenIds(Layer layer, Integer o) {
        Map<Integer, Map<Integer, Linkage>> map=getNetworkByLayer(layer);
        if(layer.equals(Layer.FROMHIDDEN)) {
            Set<Integer> keys=new FastSet<Integer>();
            for(Integer id:map.keySet()) {
                Map<Integer, Linkage> m=map.get(id);
                for(Integer id2:m.keySet()) {
                    keys.add(id2);
                }
            }
            return keys;
        } else {
            return map.keySet();
        }
    }

    public Linkage[] getLinkages(Layer layer) {
        Set<Linkage> linkages=new FastSet<Linkage>();
        Map<Integer, Map<Integer, Linkage>> map=getNetworkByLayer(layer);
        for(Integer from:map.keySet()) {
            for(Integer to:map.get(from).keySet()) {
                linkages.add(map.get(from).get(to));
            }
        }
        return linkages.toArray(new Linkage[0]);
    }
}
