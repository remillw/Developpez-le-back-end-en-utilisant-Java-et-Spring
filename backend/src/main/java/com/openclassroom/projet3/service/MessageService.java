package com.openclassroom.projet3.service;

import com.openclassroom.projet3.entities.Message;
import com.openclassroom.projet3.entities.Rental;
import com.openclassroom.projet3.entities.User;
import com.openclassroom.projet3.repositories.MessageRepository;
import com.openclassroom.projet3.repositories.RentalRepository;
import com.openclassroom.projet3.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, RentalRepository rentalRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new message
     */
    public Message createMessage(Integer rentalId, Integer userId, String messageContent) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = new Message();
        message.setRental(rental);
        message.setUser(user);
        message.setMessage(messageContent);

        return messageRepository.save(message);
    }
}
