package com.enigmastation.dao.db4o;

import com.enigmastation.dao.NeuronDAO;
import com.enigmastation.dao.model.Neuron;
import org.springframework.stereotype.Repository;

/**
 * User: joeo
 * Date: 11/5/10
 * Time: 7:03 AM
 * <p/>
 * Copyright
 */
@Repository
public class NeuronDAOImpl extends GigaspacesBaseDAO<Neuron> implements NeuronDAO {
}
