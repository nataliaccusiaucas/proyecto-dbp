package com.hirehub.backend.chat.repository;

import com.hirehub.backend.chat.domain.Conversation;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByClientAndFreelancerAndJobRequest(
            User client,
            User freelancer,
            JobRequest jobRequest
    );

    List<Conversation> findByClientOrFreelancer(User client, User freelancer);
}
