package com.secure_ai_chat.demo.service;

import com.secure_ai_chat.demo.model.ChatLog;
import com.secure_ai_chat.demo.model.ChatSession;
import com.secure_ai_chat.demo.repository.ChatLogRepository;
import com.secure_ai_chat.demo.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ChatService {

    private final PiiRedactionService piiRedactionService;
    private final AiService aiService;
    private final OcrService ocrService;
    private final ChatLogRepository chatLogRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final CryptoService cryptoService;

    public ChatService(
            PiiRedactionService piiRedactionService,
            AiService aiService,
            OcrService ocrService,
            ChatLogRepository chatLogRepository,
            ChatSessionRepository chatSessionRepository,
            CryptoService cryptoService
    ) {
        this.piiRedactionService = piiRedactionService;
        this.aiService = aiService;
        this.ocrService = ocrService;
        this.chatLogRepository = chatLogRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.cryptoService = cryptoService;
    }

    public String processChat(Long chatId, String message, MultipartFile image) {

        ChatSession session = chatSessionRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        String userText = null;
        String ocrText = null;
        byte[] imageBytes = null;

        // USER TEXT
        if (message != null && !message.isBlank()) {
            userText = message;
        }

        // IMAGE + OCR
        if (image != null && !image.isEmpty()) {
            try {
                imageBytes = image.getBytes();
            } catch (Exception e) {
                throw new RuntimeException("Image read failed", e);
            }
            ocrText = ocrService.extractText(image);
        }

        // BUILD REDACTED TEXT FOR AI
        StringBuilder redactedBuilder = new StringBuilder();

        if (userText != null) {
            redactedBuilder.append(
                    piiRedactionService.redact(userText)
            ).append("\n");
        }

        if (ocrText != null && !ocrText.isBlank()) {
            redactedBuilder.append(
                    piiRedactionService.redact(ocrText)
            ).append("\n");
        }

        if (redactedBuilder.toString().isBlank()) {
            return "Please provide a valid message or image.";
        }

        // AI CALL
        String aiResponse = aiService.getResponse(redactedBuilder.toString());

        // SAVE (ENCRYPTED)
        ChatLog log = new ChatLog();
        log.setChatSession(session);
        log.setActualUserText(userText);
        log.setOcrExtractedText(
                cryptoService.encryptString(ocrText)
        );
        log.setRedactedText(redactedBuilder.toString());
        log.setAiResponse(aiResponse);
        log.setImageGiven(imageBytes != null);
        log.setImageData(
                cryptoService.encryptBytes(imageBytes)
        );

        chatLogRepository.save(log);

        return aiResponse;
    }
}
