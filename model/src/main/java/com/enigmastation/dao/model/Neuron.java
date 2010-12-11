package com.enigmastation.dao.model;

public class Neuron extends BaseEntity {
    String payload;
    Visibility visibility;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Neuron neuron = (Neuron) o;

        if (payload != null ? !payload.equals(neuron.payload) : neuron.payload != null) return false;
        if (visibility != neuron.visibility) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Neuron");
        sb.append("{payload='").append(payload).append('\'');
        sb.append(", visibility=").append(visibility);
        sb.append("}:"+super.toString());
        return sb.toString();
    }
}
