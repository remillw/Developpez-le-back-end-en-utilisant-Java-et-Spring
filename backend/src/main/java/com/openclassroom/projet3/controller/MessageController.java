package com.openclassroom.projet3.controller;

import com.openclassroom.projet3.dto.MessageRequest;
import com.openclassroom.projet3.dto.MessageResponse;
import com.openclassroom.projet3.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Create a new message
     */
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest request) {
        try {
            messageService.createMessage(request.getRentalId(), request.getUserId(), request.getMessage());
            return ResponseEntity.ok(new MessageResponse("Message send with success"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(null));
        }
    }
}
