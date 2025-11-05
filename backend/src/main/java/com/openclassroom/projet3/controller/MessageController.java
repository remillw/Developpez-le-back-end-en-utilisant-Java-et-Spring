package com.openclassroom.projet3.controller;

import com.openclassroom.projet3.dto.MessageRequest;
import com.openclassroom.projet3.dto.MessageResponse;
import com.openclassroom.projet3.exception.BadRequestException;
import com.openclassroom.projet3.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "API de gestion des messages")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Créer un nouveau message", description = "Envoyer un message concernant une location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message envoyé avec succès",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest request) {
        messageService.createMessage(request.getRentalId(), request.getUserId(), request.getMessage());
        return ResponseEntity.ok(new MessageResponse("Message send with success"));
    }
}
