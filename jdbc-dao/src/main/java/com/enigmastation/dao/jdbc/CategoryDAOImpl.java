package com.enigmastation.dao.jdbc;

import com.enigmastation.dao.CategoryDAO;
import com.enigmastation.dao.impl.AbstractBaseDAO;
import com.enigmastation.dao.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: joeo
 * Date: 11/23/10
 * Time: 3:18 PM
 * <p/>
 * Copyright
 */
@Repository
public class CategoryDAOImpl extends AbstractBaseDAO<Category> implements CategoryDAO {
    @Autowired
    DataSource dataSource;

    JdbcTemplate template;

    @PostConstruct
    public void init() {
        template = new JdbcTemplate(dataSource);
        try {
            template.execute("create table category (id varchar primary key, createtime bigint, updatetime bigint, category varchar, _count bigint)");
        } catch (Exception ignored) {
        }
        try {
            template.execute("create unique index cat_category on category(category)");
        } catch (Exception ignored) {
        }
    }

    @Transactional
    public Category read(Category template) {
        List<Category> categories = readMultiple(template);
        if (categories.size() > 0) {
            return categories.get(0);
        }
        return null;
    }

    @Transactional
    public Category write(Category object) {
        boolean attemptUpdate = true;
        int updates = 0;
        if (object.getId() == null) {
            object.setId(UUID.randomUUID().toString());
            object.setCreateTime(System.nanoTime());
            attemptUpdate = false;
        }
        object.setLastUpdateTime(System.nanoTime());
        if (attemptUpdate) {
            updates = this.template.update("update category set createtime=?, updatetime=?, category=?, _count=? where id=?",
                    object.getCreateTime(), object.getLastUpdateTime(),
                    object.getCategory(), object.getCount(),
                    object.getId());
        }
        if (updates == 0) {
            this.template.update("insert into category (createtime, updatetime, category, _count, id) " +
                    "values (?,?,?,?,?)", object.getCreateTime(), object.getLastUpdateTime(),
                    object.getCategory(), object.getCount(),
                    object.getId());
        }
        return object;
    }

    @Transactional
    public Category take(Category template) {
        Category category = read(template);
        remove(category);
        return category;
    }

    @Transactional
    public List<Category> readMultiple(Category template) {
        final List<Category> list = new ArrayList<Category>();
        this.template.query("select id, createtime, updatetime, category, _count from category "+
                "where id like ? and category like ?",
                new Object[]{
                        template.getId() == null ? "%" : template.getId(),
                        template.getCategory() == null ? "%" : template.getCategory(),
                }, new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        Category category = new Category();
                        category.setId(resultSet.getString(1));
                        category.setCreateTime(resultSet.getLong(2));
                        category.setLastUpdateTime(resultSet.getLong(3));
                        category.setCategory(resultSet.getString(4));
                        category.setCount(resultSet.getLong(5));
                        list.add(category);
                    }
                });
        return list;
    }

    @Transactional
    public List<Category> takeMultiple(Category template) {
        List<Category> categories = readMultiple(template);
        for (Category category : categories) {
            remove(category);
        }
        return categories;
    }

    @Transactional
    private void remove(final Category category) {
        //noinspection unchecked
        this.template.update("delete from category where id=?",category.getId());
    }
}
