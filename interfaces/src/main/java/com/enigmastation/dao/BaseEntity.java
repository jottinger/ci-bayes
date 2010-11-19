package com.enigmastation.dao;

import java.io.Serializable;

public interface BaseEntity<PKType extends Serializable> {
    PKType getId();
    void setId(PKType o);
    Long getCreateTime();
    void setCreateTime(Long createTime);
    Long getLastUpdateTime();
    void setLastUpdateTime(Long lastUpdateTime);
}
