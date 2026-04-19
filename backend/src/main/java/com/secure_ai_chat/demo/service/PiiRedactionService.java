package com.secure_ai_chat.demo.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PiiRedactionService {

    // -------- EMAIL --------
    private static final Pattern EMAIL = Pattern.compile(
            "(?i)\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}\\b"
    );

    // -------- PHONE (India + generic) --------
    private static final Pattern PHONE = Pattern.compile(
            "\\b(\\+\\d{1,3}[- ]?)?[6-9]\\d{9}\\b"
    );

    // -------- CREDIT / DEBIT CARD (16 digits, grouped) --------
    private static final Pattern CARD = Pattern.compile(
            "\\b(?:\\d{4}[- ]?){3}\\d{4}\\b"
    );

    // -------- ACCOUNT NUMBER (8–18 digits, continuous) --------
    private static final Pattern ACCOUNT_NUMBER = Pattern.compile(
            "\\b\\d{8,18}\\b"
    );

    // -------- IP ADDRESS --------
    private static final Pattern IP = Pattern.compile(
            "\\b(\\d{1,3}\\.){3}\\d{1,3}\\b"
    );

    // -------- ADDRESS --------
    // Examples: "121/1 gobi", "45 Anna Nagar", "10 Gandhi Street"
//    private static final Pattern ADDRESS = Pattern.compile(
//            "\\b\\d{1,4}([/-]\\d{1,4})?\\s+[a-zA-Z]{3,}(?:\\s+[a-zA-Z]{3,})*\\b"
//    );
    private static final Pattern ADDRESS = Pattern.compile(
            "(?i)\\b\\d{1,4}([/-]\\d{1,4})?[,\\s]+[a-zA-Z]{2,}(?:[\\s,]+[a-zA-Z]{2,})*\\b"
    );


    public String redact(String text) {

        if (text == null || text.isBlank()) {
            return text;
        }

        String result = text;

        /*
         * ORDER IS CRITICAL
         */

        // 1️⃣ Email
        result = EMAIL.matcher(result).replaceAll("[REDACTED_EMAIL]");

        // 2️⃣ Phone
        result = PHONE.matcher(result).replaceAll("******");

        // 3️⃣ Credit / Debit Card
        result = CARD.matcher(result).replaceAll("[REDACTED_CARD]");

        // 4️⃣ IP Address
        result = IP.matcher(result).replaceAll("[REDACTED_IP]");

        // 5️⃣ Address
        result = ADDRESS.matcher(result).replaceAll("[REDACTED_ADDRESS]");

        // 6️⃣ Account Number (last – after phone & card)
        result = ACCOUNT_NUMBER.matcher(result)
                .replaceAll("[REDACTED_ACCOUNT]");

        return result;
    }
}
