package team.ranunculus.entities.product;

import java.util.Objects;

public class CategoryEntity {

    public static final String ATTRIBUTE_NAME="category";
    public static final String ATTRIBUTE_NAME_PLURAL = "categories";

    public static CategoryEntity build() {
        return new CategoryEntity();
    }
    int value;
    String text;

    public int getValue() {
        return value;
    }

    public CategoryEntity setValue(int value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public CategoryEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
