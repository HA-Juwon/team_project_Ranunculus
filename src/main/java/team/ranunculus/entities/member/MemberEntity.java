package team.ranunculus.entities.member;

import java.util.Date;
import java.util.Objects;

public class MemberEntity {
    public static final String ATTRIBUTE_NAME = "memberUser";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberUsers";

    public static MemberEntity build() {
        return new MemberEntity();
    }

    private String id;
    private String password;
    private String name;
    private String addressPostal;
    private String addressPrimary;
    private String addressSecondary;
    private String telecomValue;
    private String contact;
    private String email;
    private Date policyTermsAt = new Date();
    private Date policyPrivacyAt = new Date();
    private Date policyMarketingAt = new Date();
    private String statusValue;
    private Date registeredAt = new Date();
    private boolean isAdmin = false;

    public MemberEntity() {
    }

    public MemberEntity(String id, String password, String name, String addressPostal, String addressPrimary, String addressSecondary, String telecomValue, String contact, String email, Date policyTermsAt, Date policyPrivacyAt, Date policyMarketingAt, String statusValue, Date registeredAt, boolean isAdmin) {

        this.id = id;
        this.password = password;
        this.name = name;
        this.addressPostal = addressPostal;
        this.addressPrimary = addressPrimary;
        this.addressSecondary = addressSecondary;
        this.telecomValue = telecomValue;
        this.contact = contact;
        this.email = email;
        this.policyTermsAt = policyTermsAt;
        this.policyPrivacyAt = policyPrivacyAt;
        this.policyMarketingAt = policyMarketingAt;
        this.statusValue = statusValue;
        this.registeredAt = registeredAt;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public MemberEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MemberEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public MemberEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddressPostal() {
        return addressPostal;
    }

    public MemberEntity setAddressPostal(String addressPostal) {
        this.addressPostal = addressPostal;
        return this;
    }

    public String getAddressPrimary() {
        return addressPrimary;
    }

    public MemberEntity setAddressPrimary(String addressPrimary) {
        this.addressPrimary = addressPrimary;
        return this;
    }

    public String getAddressSecondary() {
        return addressSecondary;
    }

    public MemberEntity setAddressSecondary(String addressSecondary) {
        this.addressSecondary = addressSecondary;
        return this;
    }

    public String getTelecomValue() {
        return telecomValue;
    }

    public MemberEntity setTelecomValue(String telecomValue) {
        this.telecomValue = telecomValue;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public MemberEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MemberEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getPolicyTermsAt() {
        return policyTermsAt;
    }

    public MemberEntity setPolicyTermsAt(Date policyTermsAt) {
        this.policyTermsAt = policyTermsAt;
        return this;
    }

    public Date getPolicyPrivacyAt() {
        return policyPrivacyAt;
    }

    public MemberEntity setPolicyPrivacyAt(Date policyPrivacyAt) {
        this.policyPrivacyAt = policyPrivacyAt;
        return this;
    }

    public Date getPolicyMarketingAt() {
        return policyMarketingAt;
    }

    public MemberEntity setPolicyMarketingAt(Date policyMarketingAt) {
        this.policyMarketingAt = policyMarketingAt;
        return this;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public MemberEntity setStatusValue(String statusValue) {
        this.statusValue = statusValue;
        return this;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public MemberEntity setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public MemberEntity setAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
