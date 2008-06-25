package com.enigmastation.neuralnet.impl.dao.openspaces;

import com.enigmastation.neuralnet.KeyNotFoundException;
import com.enigmastation.neuralnet.NeuralNetDAO;
import com.enigmastation.neuralnet.impl.dao.openspaces.model.Linkage;
import com.enigmastation.neuralnet.impl.dao.openspaces.model.Resolution;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OpenSpacesNeuralNetDAO implements NeuralNetDAO {
    @Autowired
    GigaSpace space;
    int nextkey = 1;

    public GigaSpace getSpace() {
        return space;
    }

    public void setSpace(GigaSpace space) {
        this.space = space;
    }

    public boolean addKey(String term, int id) {
        Resolution template = new Resolution();
        template.setId(id);
        template.setTerm(term);
        Resolution res = getSpace().read(template);
        if (res == null) {
            getSpace().write(template);
            if (id > nextkey) {
                nextkey = id + 1;
            }
            return true;
        }
        return false;
    }

    public int addKey(String key) {
        Resolution template = new Resolution();
        template.setTerm(key);
        Resolution res = getSpace().read(template);
        if (res != null) {
            return res.getId();
        }
        res = new Resolution(); // we're trying to force it to LOOK....
        template.setTerm(null);
        while (res != null) {
            template.setId(nextkey);
            res = getSpace().read(template);
            nextkey++;
        }

        template.setTerm(key);
        getSpace().write(template);
        return template.getId();
    }

    public int getId(String key) {
        Resolution template = new Resolution();
        template.setTerm(key);
        Resolution res = getSpace().read(template);
        if (res != null) {
            return res.getId();
        }
        throw new KeyNotFoundException();
    }

    public String getKey(int id) {
        Resolution template = new Resolution();
        template.setId(id);
        Resolution res = getSpace().read(template);
        if (res != null) {
            return res.getTerm();
        }
        throw new KeyNotFoundException();
    }

    public Linkage getLinkage(int layer, Integer origin, Integer dest) {
        Linkage template = new Linkage(layer, origin, dest);
        return getSpace().read(template);
    }

    public void setStrength(int layer, Integer origin, Integer dest, double v) {
        Linkage template = new Linkage(layer, origin, dest);
        getSpace().take(template);
        template.setStrength(v);
        getSpace().write(template);
    }

    public Collection<? extends Integer> getHiddenIds(int layer, Integer o) {
        Linkage template = new Linkage(layer, (layer == 0 ? o : null), (layer == 0 ? null : o));
        Linkage[] linkages = getSpace().readMultiple(template, 40000);
        List<Integer> ids = new ArrayList<Integer>();
        for (Linkage l : linkages) {
            ids.add((layer == 0 ? l.getToId() : l.getFromId()));
        }
        return ids;
    }

    public Linkage[] getLinkages(int layer) {
        Linkage template = new Linkage();
        template.setLayer(layer);
        return getSpace().readMultiple(template, 40000);
    }
}
