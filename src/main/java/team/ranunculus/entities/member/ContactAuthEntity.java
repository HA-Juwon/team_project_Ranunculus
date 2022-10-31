package team.ranunculus.entities.member;

import java.util.Date;
import java.util.Objects;

public class ContactAuthEntity {
    public static final String ATTRIBUTE_NAME = "memberContactAuth";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberContactAuths";

    public static ContactAuthEntity build() {
        return new ContactAuthEntity();
    }

    private int index;
    private String contact;
    private String code;
    private String salt;
    private Date createdAt = new Date();
    private Date expiresAt;
    private boolean isExpired = false;
    public ContactAuthEntity() {
    }
    public ContactAuthEntity(int index, String contact, String code, String salt, Date createdAt, Date expiresAt, boolean isExpired) {
        this.index = index;
        this.contact = contact;
        this.code = code;
        this.salt = salt;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isExpired = isExpired;
    }

    public int getIndex() {
        return index;
    }

    public ContactAuthEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public ContactAuthEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ContactAuthEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public ContactAuthEntity setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ContactAuthEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public ContactAuthEntity setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public ContactAuthEntity setExpired(boolean expired) {
        isExpired = expired;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactAuthEntity that = (ContactAuthEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}