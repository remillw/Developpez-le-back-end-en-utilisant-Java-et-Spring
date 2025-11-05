package com.openclassroom.projet3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RentalCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Surface is required")
    private BigDecimal surface;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private String description;


    // Constructors
    public RentalCreateRequest() {
    }

    public RentalCreateRequest(String name, BigDecimal surface, BigDecimal price, String description) {
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSurface() {
        return surface;
    }

    public void setSurface(BigDecimal surface) {
        this.surface = surface;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
