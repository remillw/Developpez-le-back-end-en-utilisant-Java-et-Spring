package com.openclassroom.projet3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.projet3.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find a user by email
     * @param email the user email
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by email
     * @param email the user email
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
