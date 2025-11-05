package com.openclassroom.projet3.controller;

import com.openclassroom.projet3.dto.MessageResponse;
import com.openclassroom.projet3.dto.RentalDto;
import com.openclassroom.projet3.dto.RentalListResponseDto;
import com.openclassroom.projet3.exception.BadRequestException;
import com.openclassroom.projet3.exception.ResourceNotFoundException;
import com.openclassroom.projet3.service.FileStorageService;
import com.openclassroom.projet3.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Locations", description = "API de gestion des locations immobilières")
@SecurityRequirement(name = "bearerAuth")
public class RentalController {

    private final RentalService rentalService;
    private final FileStorageService fileStorageService;

    public RentalController(RentalService rentalService, FileStorageService fileStorageService) {
        this.rentalService = rentalService;
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "Récupérer toutes les locations", description = "Obtenir la liste de toutes les locations disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des locations récupérée",
                    content = @Content(schema = @Schema(implementation = RentalListResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping
    public ResponseEntity<RentalListResponseDto> getAllRentals() {
        RentalListResponseDto response = rentalService.getAllRentals();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer une location par ID", description = "Obtenir les détails d'une location spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location trouvée",
                    content = @Content(schema = @Schema(implementation = RentalDto.class))),
            @ApiResponse(responseCode = "404", description = "Location non trouvée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRentalById(
            @Parameter(description = "ID de la location") @PathVariable Integer id) {
        RentalDto rental = rentalService.getRentalById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + id));
        return ResponseEntity.ok(rental);
    }

    @Operation(summary = "Créer une nouvelle location", description = "Ajouter une nouvelle location avec une image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location créée avec succès",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> createRental(
            @Parameter(description = "Nom de la location") @RequestParam("name") String name,
            @Parameter(description = "Surface en m²") @RequestParam("surface") BigDecimal surface,
            @Parameter(description = "Prix par jour") @RequestParam("price") BigDecimal price,
            @Parameter(description = "Image de la location") @RequestParam("picture") MultipartFile picture,
            @Parameter(description = "Description de la location") @RequestParam(value = "description", required = false) String description,
            @Parameter(hidden = true) Authentication authentication
    ) {
        if (picture.isEmpty()) {
            throw new BadRequestException("Picture is required");
        }

        String pictureUrl = fileStorageService.storeFile(picture);
        String ownerEmail = authentication.getName();

        rentalService.createRental(name, surface, price, pictureUrl, description, ownerEmail);
        return ResponseEntity.ok(new MessageResponse("Rental created !"));
    }

    @Operation(summary = "Mettre à jour une location", description = "Modifier les informations d'une location existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location mise à jour avec succès",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Location non trouvée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRental(
            @Parameter(description = "ID de la location") @PathVariable Integer id,
            @Parameter(description = "Nom de la location") @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Surface en m²") @RequestParam(value = "surface", required = false) BigDecimal surface,
            @Parameter(description = "Prix par jour") @RequestParam(value = "price", required = false) BigDecimal price,
            @Parameter(description = "Description de la location") @RequestParam(value = "description", required = false) String description
    ) {
        rentalService.updateRental(id, name, surface, price, description);
        return ResponseEntity.ok(new MessageResponse("Rental updated !"));
    }
}
