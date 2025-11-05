package com.openclassroom.projet3.controller;

import com.openclassroom.projet3.dto.AuthResponse;
import com.openclassroom.projet3.dto.EmptyResponse;
import com.openclassroom.projet3.dto.LoginRequest;
import com.openclassroom.projet3.dto.RegisterRequest;
import com.openclassroom.projet3.dto.UserDto;
import com.openclassroom.projet3.entities.User;
import com.openclassroom.projet3.exception.BadRequestException;
import com.openclassroom.projet3.exception.UnauthorizedException;
import com.openclassroom.projet3.service.JwtService;
import com.openclassroom.projet3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "API d'authentification pour l'inscription, connexion et récupération du profil utilisateur")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Inscription d'un nouvel utilisateur", description = "Créer un nouveau compte utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request.getName(), request.getEmail(), request.getPassword());
            String token = jwtService.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            throw new BadRequestException("Email already exists or invalid data");
        }
    }

    @Operation(summary = "Connexion utilisateur", description = "Authentifier un utilisateur et recevoir un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @Operation(summary = "Récupérer le profil utilisateur", description = "Obtenir les informations de l'utilisateur connecté",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil utilisateur récupéré",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@Parameter(hidden = true) Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        return ResponseEntity.ok(userService.convertToDto(user));
    }
}
