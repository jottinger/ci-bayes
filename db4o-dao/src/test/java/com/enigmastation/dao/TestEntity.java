package com.enigmastation.dao;

public class TestEntity extends BaseEntity{
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
