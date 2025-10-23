package com.openclassroom.projet3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroom.projet3.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Find all messages for a specific rental
     * @param rentalId the rental id
     * @return list of messages
     */
    List<Message> findByRentalId(Integer rentalId);

    /**
     * Find all messages from a specific user
     * @param userId the user id
     * @return list of messages
     */
    List<Message> findByUserId(Integer userId);
}
