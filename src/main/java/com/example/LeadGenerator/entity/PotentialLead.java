package com.example.LeadGenerator.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "merchant")
public class PotentialLead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long rawLeadId;
    private String merchantName;
    private String shopName;
    private String contactNumber;
    private Double lat;
    private Double lng;
    private String pinCode;
    private Double userRating;
    private Integer score;

    public PotentialLead(Merchant merchant, Integer score) {
        PotentialLead potentialLead = PotentialLead.builder()
                .rawLeadId(merchant.getId())
                .contactNumber(merchant.getContactNumber())
                .merchantName(merchant.getMerchantName())
                .shopName(merchant.getShopName())
                .contactNumber(merchant.getContactNumber())
                .lat(merchant.getLat())
                .lng(merchant.getLng())
                .pinCode(merchant.getPinCode())
                .userRating(merchant.getRating())
                .score(score)
                .build();
    }

}