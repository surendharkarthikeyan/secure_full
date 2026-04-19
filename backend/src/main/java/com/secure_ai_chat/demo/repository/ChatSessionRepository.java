package com.secure_ai_chat.demo.repository;

import com.secure_ai_chat.demo.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
}
