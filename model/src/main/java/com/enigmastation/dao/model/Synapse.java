package com.enigmastation.dao.model;

public class Synapse extends BaseEntity {
    String from;
    String to;
    Double strength;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Synapse synapse = (Synapse) o;

        if (from != null ? !from.equals(synapse.from) : synapse.from != null) return false;
        if (strength != null ? !strength.equals(synapse.strength) : synapse.strength != null) return false;
        if (to != null ? !to.equals(synapse.to) : synapse.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (strength != null ? strength.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Synapse");
        sb.append("{from='").append(from).append('\'');
        sb.append(", to='").append(to).append('\'');
        sb.append(", strength=").append(strength);
        sb.append("}:").append(super.toString());
        return sb.toString();
    }
}
