package com.secure_ai_chat.demo.controller;

import com.secure_ai_chat.demo.model.ChatLog;
import com.secure_ai_chat.demo.model.ChatSession;
import com.secure_ai_chat.demo.repository.ChatLogRepository;
import com.secure_ai_chat.demo.repository.ChatSessionRepository;
import com.secure_ai_chat.demo.service.ChatService;
import com.secure_ai_chat.demo.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatLogRepository chatLogRepository;
    private final CryptoService cryptoService;

    public ChatController(
            ChatService chatService,
            ChatSessionRepository chatSessionRepository,
            ChatLogRepository chatLogRepository,
            CryptoService cryptoService
    ) {
        this.chatService = chatService;
        this.chatSessionRepository = chatSessionRepository;
        this.chatLogRepository = chatLogRepository;
        this.cryptoService = cryptoService;
    }

    // =========================
    // MAIN CHAT API
    // =========================
    @PostMapping
    public ResponseEntity<String> chat(
            @RequestParam Long chatId,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        String response = chatService.processChat(chatId, message, image);
        return ResponseEntity.ok(response);
    }

    // =========================
    // CREATE CHAT SESSION
    // =========================
    @PostMapping("/session")
    public ChatSession createSession(@RequestBody Map<String, String> body) {
        ChatSession session = new ChatSession();
        session.setTitle(body.get("title"));
        return chatSessionRepository.save(session);
    }

    // =========================
    // CHAT HISTORY
    // =========================
    @GetMapping("/history")
    public List<Map<String, Object>> history() {
        return chatSessionRepository.findAll().stream().map(session -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", session.getId());
            map.put("title", session.getTitle());
            map.put("date", session.getCreatedAt().toString());
            return map;
        }).toList();
    }

    // =========================
    // LOAD CHAT LOGS
    // =========================
    @GetMapping("/{chatId}")
    public List<ChatLog> loadChat(@PathVariable Long chatId) {
        return chatLogRepository.findByChatSessionId(chatId);
    }

    // =========================
    // LOAD IMAGE (DECRYPTED)
    // =========================
    @GetMapping("/image/{logId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long logId) {

        ChatLog log = chatLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (log.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] decryptedImage =
                cryptoService.decryptBytes(log.getImageData());

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(decryptedImage);
    }

    // =========================
    // DELETE CHAT SESSION
    // =========================
    @DeleteMapping("/{chatId}")
    public void deleteChat(@PathVariable Long chatId) {
        chatSessionRepository.deleteById(chatId);
    }
}
