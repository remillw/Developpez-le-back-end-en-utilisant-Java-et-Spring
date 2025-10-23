package com.openclassroom.projet3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.projet3.entities.Rental;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    /**
     * Find all rentals by owner id
     * @param ownerId the owner user id
     * @return list of rentals
     */
    List<Rental> findByOwnerId(Integer ownerId);
}
