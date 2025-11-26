package com.hirehub.backend.chat.service;

import com.hirehub.backend.chat.domain.Conversation;
import com.hirehub.backend.chat.repository.ConversationRepository;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.common.exception.ResourceNotFoundException;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public ConversationService(ConversationRepository conversationRepository,
                               UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public Conversation getOrCreateForOffer(User client, User freelancer, JobRequest jobRequest) {

        return conversationRepository
                .findByClientAndFreelancerAndJobRequest(client, freelancer, jobRequest)
                .orElseGet(() -> {
                    Conversation c = new Conversation(client, freelancer, jobRequest);
                    return conversationRepository.save(c);
                });
    }

    public List<Conversation> getConversationsForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return conversationRepository.findByClientOrFreelancer(user, user);
    }

    public Conversation getById(UUID id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada"));
    }
}
