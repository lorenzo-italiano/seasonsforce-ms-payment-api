package fr.polytech.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RecruiterDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private Boolean isRegistered;
    private Date birthdate;
    private String citizenship;
    private String phone;
    private UUID addressId;
    private String profilePictureUrl;
    private Integer gender;
    private UUID companyId;
    private UUID planId;
    private List<UUID> offerIdList;
    private List<UUID> paymentIdList;

    public RecruiterDTO() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getPlanId() {
        return planId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public List<UUID> getOfferIdList() {
        return offerIdList;
    }

    public void setOfferIdList(List<UUID> offerIdList) {
        this.offerIdList = offerIdList;
    }

    public List<UUID> getPaymentIdList() {
        return paymentIdList;
    }

    public void setPaymentIdList(List<UUID> paymentIdList) {
        this.paymentIdList = paymentIdList;
    }
}
