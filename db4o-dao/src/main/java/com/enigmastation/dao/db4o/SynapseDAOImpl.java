package com.enigmastation.dao.db4o;

import com.enigmastation.dao.SynapseDAO;
import com.enigmastation.dao.model.Synapse;
import org.springframework.stereotype.Repository;

/**
 * User: joeo
 * Date: 11/5/10
 * Time: 7:03 AM
 * <p/>
 * Copyright
 */
@Repository
public class SynapseDAOImpl extends GigaspacesBaseDAO<Synapse> implements SynapseDAO {
}
