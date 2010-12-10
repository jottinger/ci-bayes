package com.enigmastation.dao.gigaspaces;

import com.enigmastation.dao.FeatureDAO;
import com.enigmastation.dao.model.Feature;
import org.springframework.stereotype.Repository;

/**
 * User: joeo
 * Date: 11/28/10
 * Time: 8:41 AM
 * <p/>
 * Copyright
 */
@Repository
public class FeatureDAOImpl extends GigaspacesBaseDAO<Feature> implements FeatureDAO {
}
