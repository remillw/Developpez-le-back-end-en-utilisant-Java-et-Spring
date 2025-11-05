package com.openclassroom.projet3.dto;

import java.util.List;

public class RentalListResponseDto {

    private List<RentalDto> rentals;

    // Constructors
    public RentalListResponseDto() {
    }

    public RentalListResponseDto(List<RentalDto> rentals) {
        this.rentals = rentals;
    }

    // Getters and Setters
    public List<RentalDto> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalDto> rentals) {
        this.rentals = rentals;
    }
}
