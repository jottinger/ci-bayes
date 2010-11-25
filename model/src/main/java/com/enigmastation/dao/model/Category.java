package com.enigmastation.dao.model;

import com.enigmastation.dao.BaseEntity;

/**
 * User: joeo
 * Date: 11/23/10
 * Time: 3:17 PM
 * <p/>
 * Copyright
 */
public class Category extends BaseEntity {
    String category;
    Long count;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Category");
        sb.append("{category='").append(category).append('\'');
        sb.append(", count=").append(count);
        sb.append("}:").append(super.toString());
        return sb.toString();
    }
}
