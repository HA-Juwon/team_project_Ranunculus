package team.ranunculus.entities.member;

import java.util.Objects;

public class TelecomEntity {
    public static final String ATTRIBUTE_NAME = "memberTelecom";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberTelecoms";

    public static TelecomEntity build() {
        return new TelecomEntity();
    }

    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public TelecomEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public TelecomEntity setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelecomEntity that = (TelecomEntity) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
