package com.openclassroom.projet3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {

    @NotNull(message = "Rental ID is required")
    @JsonProperty("rental_id")
    private Integer rentalId;

    @NotNull(message = "User ID is required")
    @JsonProperty("user_id")
    private Integer userId;

    @NotBlank(message = "Message is required")
    private String message;

    // Constructors
    public MessageRequest() {
    }

    public MessageRequest(Integer rentalId, Integer userId, String message) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.message = message;
    }

    // Getters and Setters
    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
