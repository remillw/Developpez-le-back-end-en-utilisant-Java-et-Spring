package com.openclassroom.projet3.dto;

import java.math.BigDecimal;

public class RentalUpdateRequest {

    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String description;

    // Note: picture is handled as MultipartFile in controller, not in DTO

    // Constructors
    public RentalUpdateRequest() {
    }

    public RentalUpdateRequest(String name, BigDecimal surface, BigDecimal price, String description) {
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
