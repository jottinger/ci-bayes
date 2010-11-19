package com.enigmastation.dao.impl;

import com.enigmastation.dao.BaseEntity;

public abstract class AbstractBaseEntity implements BaseEntity<String> {
    String id;
    Long createTime;
    Long lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBaseEntity)) return false;

        AbstractBaseEntity that = (AbstractBaseEntity) o;

        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AbstractBaseEntity");
        sb.append("{id='").append(id).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", lastUpdateTime=").append(lastUpdateTime);
        sb.append('}');
        return sb.toString();
    }
}
