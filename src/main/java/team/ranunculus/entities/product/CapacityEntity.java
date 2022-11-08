package team.ranunculus.entities.product;

import java.util.Objects;

public class CapacityEntity {

    public static final String ATTRIBUTE_NAME = "capacity";

    public static CapacityEntity build() {
        return new CapacityEntity();
    }

    int value;
    String text;

    public int getValue() {
        return value;
    }

    public CapacityEntity setValue(int value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public CapacityEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapacityEntity that = (CapacityEntity) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
