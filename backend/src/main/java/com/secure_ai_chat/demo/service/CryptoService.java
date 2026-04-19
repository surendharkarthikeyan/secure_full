package com.secure_ai_chat.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    private static final String ALGORITHM = "AES";

    private final SecretKeySpec secretKey;

    public CryptoService(@Value("${crypto.secret}") String key) {
        // AES requires 16 / 24 / 32 bytes
        byte[] keyBytes = key.getBytes();
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    // 🔐 STRING ENCRYPT
    public String encryptString(String data) {
        if (data == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("String encryption failed", e);
        }
    }

    // 🔓 STRING DECRYPT
    public String decryptString(String encryptedData) {
        if (encryptedData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("String decryption failed", e);
        }
    }

    // 🔐 BYTE[] ENCRYPT
    public byte[] encryptBytes(byte[] data) {
        if (data == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Byte encryption failed", e);
        }
    }

    // 🔓 BYTE[] DECRYPT
    public byte[] decryptBytes(byte[] encryptedData) {
        if (encryptedData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Byte decryption failed", e);
        }
    }
}
