package com.stockpilot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SupplierDTO {

    private Long id;

    @NotBlank(message = "Supplier name is required")
    @Size(max = 255)
    private String name;

    @Email(message = "Invalid email format")
    @Size(max = 255)
    private String contactEmail;

    @Size(max = 50)
    private String phone;

    public SupplierDTO() {
    }

    public SupplierDTO(Long id, String name, String contactEmail, String phone) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
