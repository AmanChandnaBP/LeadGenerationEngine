package com.example.LeadGenerator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "refferal_link", nullable = true)
    private String refferallink;

    @Column(name = "count_of_referred")
    private Integer countOfReferred;

    @Column(name = "merchant_ids", columnDefinition = "TEXT")
    private String merchantIds;

    @Transient
    private List<Long> merchantIdsList;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        hash = generateHash(name, mobileNumber);
    }

    @PostLoad
    private void loadMerchantIdsList() {
        if (this.merchantIds != null && !this.merchantIds.isEmpty()) {
            this.merchantIdsList = Arrays.stream(this.merchantIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
    }

    @PreUpdate
    private void saveMerchantIds() {
        if (this.merchantIdsList != null) {
            this.merchantIds = this.merchantIdsList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }
    }

    private String generateHash(String name, String mobileNumber) {
        try {
            String combined = name + mobileNumber;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combined.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
