package com.wedding.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// VIDURA IT25102240
// OOP: Utility class — password hashing using SHA-256

public class PasswordUtil {

    public static String hash(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    // Check if a plain-text password matches a stored hash
    public static boolean verify(String plainText, String storedHash) {
        return hash(plainText).equals(storedHash);
    }
}
