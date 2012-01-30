package com.enigmastation.dao.objectify;

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
public class SynapseDAOImpl extends Db4OBaseDAO<Synapse> implements SynapseDAO {
}
