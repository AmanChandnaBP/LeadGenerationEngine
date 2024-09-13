//package com.example.LeadGenerator.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "merchant_photos")
//@Data
//@NoArgsConstructor
//public class Photo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String photoReference;
//    private Integer width;
//    private Integer height;
//
//    @ManyToOne
//    @JoinColumn(name = "merchant_id", nullable = false)
//    private Merchant merchant;
//}
