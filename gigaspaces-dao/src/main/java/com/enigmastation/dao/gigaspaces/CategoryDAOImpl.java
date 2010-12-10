package com.enigmastation.dao.gigaspaces;

import com.enigmastation.dao.CategoryDAO;
import com.enigmastation.dao.model.Category;
import org.springframework.stereotype.Repository;

/**
 * User: joeo
 * Date: 11/28/10
 * Time: 8:40 AM
 * <p/>
 * Copyright
 */
@Repository
public class CategoryDAOImpl extends GigaspacesBaseDAO<Category> implements CategoryDAO {
}
