package com.openclassroom.projet3.service;

import com.openclassroom.projet3.dto.RentalDto;
import com.openclassroom.projet3.dto.RentalListResponseDto;
import com.openclassroom.projet3.entities.Rental;
import com.openclassroom.projet3.entities.User;
import com.openclassroom.projet3.repositories.RentalRepository;
import com.openclassroom.projet3.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all rentals
     * @return RentalListResponseDto containing all rentals
     */
    public RentalListResponseDto getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        List<RentalDto> rentalDtos = rentals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new RentalListResponseDto(rentalDtos);
    }

    /**
     * Get rental by id
     * @param id rental id
     * @return Optional of RentalDto
     */
    public Optional<RentalDto> getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .map(this::convertToDto);
    }

    /**
     * Create a new rental
     */
    public Rental createRental(String name, BigDecimal surface, BigDecimal price, String picture, String description, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setPicture(picture);
        rental.setDescription(description);
        rental.setOwner(owner);

        return rentalRepository.save(rental);
    }

    /**
     * Update a rental
     */
    public Optional<Rental> updateRental(Integer id, String name, BigDecimal surface, BigDecimal price, String description) {
        Optional<Rental> rentalOpt = rentalRepository.findById(id);

        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();

            if (name != null) rental.setName(name);
            if (surface != null) rental.setSurface(surface);
            if (price != null) rental.setPrice(price);
            if (description != null) rental.setDescription(description);

            return Optional.of(rentalRepository.save(rental));
        }

        return Optional.empty();
    }

    /**
     * Convert Rental entity to RentalDto
     * @param rental the rental entity
     * @return RentalDto
     */
    private RentalDto convertToDto(Rental rental) {
        return new RentalDto(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt(),
                rental.getUpdatedAt()
        );
    }
}
