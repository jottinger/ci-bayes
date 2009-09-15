package com.enigmastation.neuralnet.impl;

public class Synapse {
    Integer source;
    Integer destination;
    Double weight;
    Boolean hidden;

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Synapse(int source, int destination, double weight, boolean hidden) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.hidden = hidden;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Synapse synapse = (Synapse) o;

        if (!destination.equals(synapse.destination)) return false;
        if (!source.equals(synapse.source)) return false;
        if (!hidden.equals(synapse.hidden)) return false;

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Synapse");
        sb.append("{source=").append(source);
        sb.append(", destination=").append(destination);
        sb.append(", hidden=").append(hidden);
        sb.append(", weight=").append(weight);
        sb.append(", this=").append(super.toString());
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (hidden != null ? hidden.hashCode() : 0);
        return result;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
