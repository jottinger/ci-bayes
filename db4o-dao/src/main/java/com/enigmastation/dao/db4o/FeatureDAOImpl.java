package com.enigmastation.dao.db4o;

import com.enigmastation.dao.FeatureDAO;
import com.enigmastation.dao.model.Feature;
import org.springframework.stereotype.Repository;

/**
 * User: joeo
 * Date: 11/23/10
 * Time: 3:18 PM
 * <p/>
 * Copyright
 */
@Repository
public class FeatureDAOImpl extends Db4OBaseDAO<Feature> implements FeatureDAO {
}
