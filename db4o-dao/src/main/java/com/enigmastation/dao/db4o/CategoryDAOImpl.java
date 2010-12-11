package com.enigmastation.dao.db4o;

import com.enigmastation.dao.CategoryDAO;
import com.enigmastation.dao.model.Category;
import org.springframework.stereotype.Repository;

/**
 * User: joeo
 * Date: 11/23/10
 * Time: 3:18 PM
 * <p/>
 * Copyright
 */
@Repository
public class CategoryDAOImpl extends Db4OBaseDAO<Category> implements CategoryDAO {
}
