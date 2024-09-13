//package com.example.LeadGenerator.Service;
//
//package com.example.referral.utils;
//
//import org.springframework.stereotype.Component;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//@Component
//public class HashGenerator {
//
//    public String generateRefereeID(String refereePhone, String refereeName) {
//        String input = refereePhone;
//        System.out.println("......" + input);
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashBytes = digest.digest(input.getBytes());
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : hashBytes) {
//                hexString.append(String.format("%02x", b));
//            }
//            return hexString.substring(0, 10);  // Get first 10 characters
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
