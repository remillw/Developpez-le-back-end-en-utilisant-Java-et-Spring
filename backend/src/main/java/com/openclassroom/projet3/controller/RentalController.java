package com.openclassroom.projet3.controller;

import com.openclassroom.projet3.dto.MessageResponse;
import com.openclassroom.projet3.dto.RentalDto;
import com.openclassroom.projet3.dto.RentalListResponseDto;
import com.openclassroom.projet3.service.FileStorageService;
import com.openclassroom.projet3.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final FileStorageService fileStorageService;

    public RentalController(RentalService rentalService, FileStorageService fileStorageService) {
        this.rentalService = rentalService;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Get all rentals
     * @return RentalListResponseDto containing all rentals
     */
    @GetMapping
    public ResponseEntity<RentalListResponseDto> getAllRentals() {
        RentalListResponseDto response = rentalService.getAllRentals();
        return ResponseEntity.ok(response);
    }

    /**
     * Get rental by id
     * @param id rental id
     * @return RentalDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Integer id) {
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new rental
     */
    @PostMapping
    public ResponseEntity<MessageResponse> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication
    ) {
        try {
            String pictureUrl = fileStorageService.storeFile(picture);
            String ownerEmail = authentication.getName();

            rentalService.createRental(name, surface, price, pictureUrl, description, ownerEmail);
            return ResponseEntity.ok(new MessageResponse("Rental created !"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(null));
        }
    }

    /**
     * Update a rental
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRental(
            @PathVariable Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "surface", required = false) BigDecimal surface,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "description", required = false) String description
    ) {
        try {
            rentalService.updateRental(id, name, surface, price, description);
            return ResponseEntity.ok(new MessageResponse("Rental updated !"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(null));
        }
    }
}
