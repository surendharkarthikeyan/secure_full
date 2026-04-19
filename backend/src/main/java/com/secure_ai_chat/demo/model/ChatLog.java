package com.secure_ai_chat.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "secure_chat_log")
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ What user typed
    @Column(name = "actual_user_text", columnDefinition = "TEXT")
    private String actualUserText;

    // ❌ Internal OCR text (never shown)
    @Column(name = "ocr_extracted_text", columnDefinition = "TEXT")
    private String ocrExtractedText;

    // ❌ Redacted text sent to AI
    @Column(name = "redacted_text", columnDefinition = "TEXT", nullable = false)
    private String redactedText;

    // ✅ AI reply
    @Column(name = "ai_response", columnDefinition = "TEXT", nullable = false)
    private String aiResponse;

    // ✅ User image
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(name = "image_given", nullable = false)
    private boolean imageGiven;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatSession chatSession;

    // getters & setters
    public Long getId() { return id; }

    public String getActualUserText() { return actualUserText; }
    public void setActualUserText(String actualUserText) {
        this.actualUserText = actualUserText;
    }

    public String getOcrExtractedText() { return ocrExtractedText; }
    public void setOcrExtractedText(String ocrExtractedText) {
        this.ocrExtractedText = ocrExtractedText;
    }

    public String getRedactedText() { return redactedText; }
    public void setRedactedText(String redactedText) {
        this.redactedText = redactedText;
    }

    public String getAiResponse() { return aiResponse; }
    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public boolean isImageGiven() { return imageGiven; }
    public void setImageGiven(boolean imageGiven) {
        this.imageGiven = imageGiven;
    }

    public ChatSession getChatSession() { return chatSession; }
    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }
}
