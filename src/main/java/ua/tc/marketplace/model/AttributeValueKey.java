package ua.tc.marketplace.model;

import lombok.Data;

import java.util.Objects;

@Data
public class AttributeValueKey {
    private String name;
    private String value;

    public AttributeValueKey(String name, String value) {
        this.name = name;
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValueKey that = (AttributeValueKey) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}