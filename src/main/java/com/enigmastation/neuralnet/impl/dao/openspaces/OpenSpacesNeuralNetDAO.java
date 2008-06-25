package com.enigmastation.neuralnet.impl.dao.openspaces;

import com.enigmastation.neuralnet.KeyNotFoundException;
import com.enigmastation.neuralnet.NeuralNetDAO;
import com.enigmastation.neuralnet.impl.dao.openspaces.model.GigaspacesLinkage;
import com.enigmastation.neuralnet.impl.dao.openspaces.model.GigaspacesResolution;
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
        GigaspacesResolution template = new GigaspacesResolution();
        template.setId(id);
        template.setTerm(term);
        GigaspacesResolution res = getSpace().read(template);
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
        GigaspacesResolution template = new GigaspacesResolution();
        template.setTerm(key);
        GigaspacesResolution res = getSpace().read(template);
        if (res != null) {
            return res.getId();
        }
        res = new GigaspacesResolution(); // we're trying to force it to LOOK....
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
        GigaspacesResolution template = new GigaspacesResolution();
        template.setTerm(key);
        GigaspacesResolution res = getSpace().read(template);
        if (res != null) {
            return res.getId();
        }
        throw new KeyNotFoundException();
    }

    public String getKey(int id) {
        GigaspacesResolution template = new GigaspacesResolution();
        template.setId(id);
        GigaspacesResolution res = getSpace().read(template);
        if (res != null) {
            return res.getTerm();
        }
        throw new KeyNotFoundException();
    }

    public GigaspacesLinkage getLinkage(int layer, Integer origin, Integer dest) {
        GigaspacesLinkage template = new GigaspacesLinkage(layer, origin, dest);
        return getSpace().read(template);
    }

    public void setStrength(int layer, Integer origin, Integer dest, double v) {
        GigaspacesLinkage template = new GigaspacesLinkage(layer, origin, dest);
        getSpace().take(template);
        template.setStrength(v);
        getSpace().write(template);
    }

    public Collection<? extends Integer> getHiddenIds(int layer, Integer o) {
        GigaspacesLinkage template = new GigaspacesLinkage(layer, (layer == 0 ? o : null), (layer == 0 ? null : o));
        GigaspacesLinkage[] linkages = getSpace().readMultiple(template, 40000);
        List<Integer> ids = new ArrayList<Integer>();
        for (GigaspacesLinkage l : linkages) {
            ids.add((layer == 0 ? l.getToId() : l.getFromId()));
        }
        return ids;
    }

    public GigaspacesLinkage[] getLinkages(int layer) {
        GigaspacesLinkage template = new GigaspacesLinkage();
        template.setLayer(layer);
        return getSpace().readMultiple(template, 40000);
    }
}
