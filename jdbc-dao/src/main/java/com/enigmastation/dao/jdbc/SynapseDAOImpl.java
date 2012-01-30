package com.enigmastation.dao.jdbc;

import com.enigmastation.dao.NeuronDAO;
import com.enigmastation.dao.impl.AbstractBaseDAO;
import com.enigmastation.dao.model.Neuron;
import com.enigmastation.dao.model.Visibility;
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
public class SynapseDAOImpl extends AbstractBaseDAO<Neuron> implements NeuronDAO {
    @Autowired
    DataSource dataSource;

    JdbcTemplate template;

    @PostConstruct
    public void init() {
        template = new JdbcTemplate(dataSource);
        try {
            template.execute("create table category (id varchar primary key, createtime bigint, updatetime bigint, payload varchar, visibility number)");
        } catch (Exception ignored) {
        }
    }

    @Transactional
    public Neuron read(Neuron template) {
        List<Neuron> categories = readMultiple(template);
        if (categories.size() > 0) {
            return categories.get(0);
        }
        return null;
    }

    @Transactional
    public Neuron write(Neuron object) {
        boolean attemptUpdate = true;
        int updates = 0;
        if (object.getId() == null) {
            object.setId(UUID.randomUUID().toString());
            object.setCreateTime(System.nanoTime());
            attemptUpdate = false;
        }
        object.setLastUpdateTime(System.nanoTime());
        if (attemptUpdate) {
            updates = this.template.update("update neuron set createtime=?, updatetime=?, payload=?, visibility=? where id=?",
                    object.getCreateTime(), object.getLastUpdateTime(),
                    object.getPayload(), object.getVisibility().getStrength(),
                    object.getId());
        }
        if (updates == 0) {
            this.template.update("insert into category (createtime, updatetime, payload, visibility, id) " +
                    "values (?,?,?,?,?)", object.getCreateTime(), object.getLastUpdateTime(),
                    object.getPayload(), object.getVisibility(),
                    object.getId());
        }
        return object;
    }

    @Transactional
    public Neuron take(Neuron template) {
        Neuron neuron = read(template);
        remove(neuron);
        return neuron;
    }

    @Transactional
    public List<Neuron> readMultiple(Neuron template) {
        final List<Neuron> list = new ArrayList<Neuron>();
        this.template.query("select id, createtime, updatetime, payload, visibility from category "+
                "where id like ? and payload like ?",
                new Object[]{
                        template.getId() == null ? "%" : template.getId(),
                        template.getPayload() == null ? "%" : template.getPayload(),
                }, new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        Neuron category = new Neuron();
                        category.setId(resultSet.getString(1));
                        category.setCreateTime(resultSet.getLong(2));
                        category.setLastUpdateTime(resultSet.getLong(3));
                        category.setPayload(resultSet.getString(4));
                        category.setVisibility(resultSet.getDouble(5) == Visibility.VISIBLE.getStrength()?Visibility.VISIBLE:Visibility.INVISIBLE);
                        list.add(category);
                    }
                });
        return list;
    }

    @Transactional
    public List<Neuron > takeMultiple(Neuron template) {
        List<Neuron> neurons = readMultiple(template);
        for (Neuron category : neurons) {
            remove(category);
        }
        return neurons;
    }

    @Transactional
    private void remove(final Neuron category) {
        //noinspection unchecked
        this.template.update("delete from neuron where id=?", category.getId());
    }
}
