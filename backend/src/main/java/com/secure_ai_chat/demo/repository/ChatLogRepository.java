package com.secure_ai_chat.demo.repository;

import com.secure_ai_chat.demo.model.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    List<ChatLog> findByChatSessionId(Long chatId);
}
