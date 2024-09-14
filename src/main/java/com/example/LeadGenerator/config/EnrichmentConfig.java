package com.example.LeadGenerator.config;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = EnrichmentConfig.PREFIX)
public class EnrichmentConfig {

    public static final String PREFIX = "enrichment";
    private Integer NotPermanentlyClosedRuleScore = 100;
    private Integer BusinessStatusOperationalRuleScore = 90;
    private Integer RatingThreshold = 4.0;
    private Integer RatingAboveThresholdScore = 80;
    private Integer IconUriNotEmptyRuleScore = 70;
    private Integer ShopNameNotEmptyRuleScore = 60;
    private Integer ValidContactNumberRuleScore = 50;
    private Integer ValidLatitudeLongitudeRuleScore = 40;
    private Integer ValidPinCodeRuleScore = 30;
    private Integer MerchantNameNotEmptyRuleScore = 60;
}