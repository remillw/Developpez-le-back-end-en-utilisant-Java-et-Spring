package com.openclassroom.projet3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.projet3.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
