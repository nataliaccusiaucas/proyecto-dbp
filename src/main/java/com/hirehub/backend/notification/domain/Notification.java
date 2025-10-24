package com.hirehub.backend.notification.domain;

import com.hirehub.backend.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    private boolean read = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification() {}

    public Notification(NotificationType type, String message, User recipient) {
        this.type = type;
        this.message = message;
        this.recipient = recipient;
    }

    public UUID getId() { return id; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
    public User getRecipient() { return recipient; }
    public boolean isRead() { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }


    public void setRead(boolean read) { this.read = read; }
}
