package com.enigmastation.dao.jdbc;

import com.enigmastation.dao.FeatureDAO;
import com.enigmastation.dao.impl.AbstractBaseDAO;
import com.enigmastation.dao.model.Feature;
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
public class FeatureDAOImpl extends AbstractBaseDAO<Feature> implements FeatureDAO {
    @Autowired
    DataSource dataSource;

    JdbcTemplate template;

    @PostConstruct
    public void init() {
        template = new JdbcTemplate(dataSource);
        try {
            template.execute("create table feature (id varchar primary key, createtime bigint, updatetime bigint, feature varchar, category varchar, _count bigint)");
        } catch (Exception ignored) {
        }
        try {
            template.execute("create unique index feat_category on feature(feature, category)");
        } catch (Exception ignored) {
        }
        try {
            template.execute("create index feat_feature on feature(feature)");
        } catch (Exception ignored) {
        }
    }

    @Transactional
    public Feature read(Feature template) {
        List<Feature> features = readMultiple(template);
        if (features.size() > 0) {
            return features.get(0);
        }
        return null;
    }

    @Transactional
    public Feature write(Feature object) {
        boolean attemptUpdate = true;
        int updates = 0;
        if (object.getId() == null) {
            object.setId(UUID.randomUUID().toString());
            object.setCreateTime(System.nanoTime());
            attemptUpdate = false;
        }
        object.setLastUpdateTime(System.nanoTime());
        if (attemptUpdate) {
            updates = this.template.update("update feature set createtime=?, updatetime=?, feature=?, category=?, _count=? where id=?",
                    object.getCreateTime(), object.getLastUpdateTime(),
                    object.getFeature(), object.getCategory(), object.getCount(),
                    object.getId());
        }
        if (updates == 0) {
            updates = this.template.update("insert into feature (createtime, updatetime, feature, category, _count, id) " +
                    "values (?,?,?,?,?,?)", object.getCreateTime(), object.getLastUpdateTime(),
                    object.getFeature(), object.getCategory(), object.getCount(),
                    object.getId());
            if (updates == 0) {
                // we have an error condition! Someone wanted to update something that already existed.
            }
        }
        return object;
    }

    @Transactional
    public Feature take(Feature template) {
        Feature feature = read(template);
        remove(feature);
        return feature;
    }

    @Transactional
    public List<Feature> readMultiple(Feature template) {
        final List<Feature> list = new ArrayList<Feature>();
        this.template.query("select id, createtime, updatetime, feature, category, _count from feature where id like ? " +
                " and feature like ? and category like ?",
                new Object[]{
                        template.getId() == null ? "%" : template.getId(),
                        template.getFeature() == null ? "%" : template.getFeature(),
                        template.getCategory() == null ? "%" : template.getCategory(),
                }, new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        Feature f = new Feature();
                        f.setId(resultSet.getString(1));
                        f.setCreateTime(resultSet.getLong(2));
                        f.setLastUpdateTime(resultSet.getLong(3));
                        f.setFeature(resultSet.getString(4));
                        f.setCategory(resultSet.getString(5));
                        f.setCount(resultSet.getLong(6));
                        list.add(f);
                    }
                });
        return list;
    }

    @Transactional
    public List<Feature> takeMultiple(Feature template) {
        List<Feature> features = readMultiple(template);
        for (Feature f : features) {
            remove(f);
        }
        return features;
    }

    @Transactional
    private void remove(final Feature f) {
        this.template.update("delete from feature where id=?", f.getId());
    }
}
