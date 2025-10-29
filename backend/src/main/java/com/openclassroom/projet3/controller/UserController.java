package com.openclassroom.projet3.controller;

import com.openclassroom.projet3.dto.UserDto;
import com.openclassroom.projet3.entities.User;
import com.openclassroom.projet3.repositories.UserRepository;
import com.openclassroom.projet3.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Get user by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        Optional<User> userOpt = userRepository.findById(id);

        return userOpt
                .map(user -> ResponseEntity.ok(userService.convertToDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}
