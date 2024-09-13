package com.example.LeadGenerator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "merchant")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String placeId;
    private String businessStatus;
    private String iconUrl;
    private Double rating;
    private Integer userRatingsTotal;
    private Boolean permanentlyClosed;
    private String vicinity;

    // Add latitude and longitude fields directly
    private Double lat;
    private Double lng;

    // Southwest and northeast coordinates for viewport
    private Double southwestLat;
    private Double southwestLng;
    private Double northeastLat;
    private Double northeastLng;
}
