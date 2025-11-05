package com.openclassroom.projet3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.projet3.entities.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
