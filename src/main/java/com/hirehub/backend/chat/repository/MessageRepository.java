package com.hirehub.backend.chat.repository;

import com.hirehub.backend.chat.domain.Conversation;
import com.hirehub.backend.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);
}
