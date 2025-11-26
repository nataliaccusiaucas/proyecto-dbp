package com.hirehub.backend.chat.controller;

import com.hirehub.backend.chat.domain.Conversation;
import com.hirehub.backend.chat.domain.Message;
import com.hirehub.backend.chat.dto.ConversationResponseDTO;
import com.hirehub.backend.chat.dto.MessageResponseDTO;
import com.hirehub.backend.chat.dto.SendMessageRequestDTO;
import com.hirehub.backend.chat.service.ConversationService;
import com.hirehub.backend.chat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    public ChatController(ConversationService conversationService,
                          MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/conversations/user/{userId}")
    public ResponseEntity<List<ConversationResponseDTO>> listForUser(@PathVariable UUID userId) {
        List<ConversationResponseDTO> response = conversationService
                .getConversationsForUser(userId)
                .stream()
                .map(c -> new ConversationResponseDTO(
                        c.getId(),
                        c.getJobRequest().getTitle(),
                        c.getClient().getName(),
                        c.getFreelancer().getName(),
                        c.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<MessageResponseDTO>> listMessages(@PathVariable UUID conversationId) {
        Conversation c = conversationService.getById(conversationId);
        List<Message> messages = messageService.getMessages(c);

        List<MessageResponseDTO> resp = messages.stream()
                .map(m -> new MessageResponseDTO(
                        m.getId(),
                        m.getSender().getId(),
                        m.getSender().getName(),
                        m.getContent(),
                        m.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<MessageResponseDTO> send(
            @PathVariable UUID conversationId,
            @RequestBody SendMessageRequestDTO dto
    ) {
        Conversation c = conversationService.getById(conversationId);

        var msg = messageService.sendMessage(c, UUID.fromString(dto.senderId()), dto.content());

        var resp = new MessageResponseDTO(
                msg.getId(),
                msg.getSender().getId(),
                msg.getSender().getName(),
                msg.getContent(),
                msg.getCreatedAt()
        );

        return ResponseEntity.ok(resp);
    }
}
