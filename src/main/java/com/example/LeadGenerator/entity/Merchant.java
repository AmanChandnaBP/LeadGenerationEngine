package com.example.LeadGenerator.entity;

import com.example.LeadGenerator.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Builder
@Table(name = "merchant")
@AllArgsConstructor
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;
    private String placeId;
    private String businessStatus;
    private String iconUrl;
    private Double rating;
    private Integer userRatingsTotal;
    private Boolean permanentlyClosed;
    private String vicinity;
    private String ContactNumber;
    private String merchantName;
    private String pinCode;
    private String failedRules;
    private String status;

    // Add latitude and longitude fields directly
    private Double lat;
    private Double lng;

    // Southwest and northeast coordinates for viewport
    private Double southwestLat;
    private Double southwestLng;
    private Double northeastLat;
    private Double northeastLng;
    private String formatted_address;
    private String placeDetailsName;
    private String source;
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

}
