//package com.example.LeadGenerator.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "geometry")
//@Data
//@NoArgsConstructor
//public class Geometry {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "merchant_id")
//    private Merchant merchant;
//
//    private Double lat;
//    private Double lng;
//    private Double southwestLat;
//    private Double southwestLng;
//    private Double northeastLat;
//    private Double northeastLng;
//}
