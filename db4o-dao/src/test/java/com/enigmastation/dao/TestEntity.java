package com.enigmastation.dao;

import com.enigmastation.dao.db4o.Db4OBaseEntity;

public class TestEntity extends Db4OBaseEntity{
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TestEntity");
        sb.append("{text='").append(text).append('\'');
        sb.append("}:"+super.toString());
        return sb.toString();
    }
}
