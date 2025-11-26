package com.hirehub.backend.chat.service;

import com.hirehub.backend.chat.domain.Conversation;
import com.hirehub.backend.chat.domain.Message;
import com.hirehub.backend.chat.repository.MessageRepository;
import com.hirehub.backend.common.exception.ResourceNotFoundException;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<Message> getMessages(Conversation conversation) {
        return messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
    }

    public Message sendMessage(Conversation conversation, UUID senderId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Message message = new Message(conversation, sender, content);
        return messageRepository.save(message);
    }
}
