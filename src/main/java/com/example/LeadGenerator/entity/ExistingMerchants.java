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
@Table(name = "exsisting_merchant")
public class ExistingMerchants {

    @Id
    private Long id;

    @Column(name = "business_category")
    private String businessCategory;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "mid")
    private String mid;

    @Column(name = "mobile")
    private String mobile;

}
