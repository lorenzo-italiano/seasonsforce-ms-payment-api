package fr.polytech.model;

import java.util.Date;

public class InvoiceDataDTO {
    private Date creationDate;
    private String name;
    private String surname;
    private String address;
    private String plan;
    private double price;

    public InvoiceDataDTO() {
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean hasInvalidFields() {
        return creationDate == null || name == null || surname == null || address == null || plan == null || price == 0;
    }
}
