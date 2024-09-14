package com.example.LeadGenerator.Service.enrichment.ruleImpl;

import com.example.LeadGenerator.Service.enrichment.Rule;
import com.example.LeadGenerator.entity.Merchant;
import lombok.Getter;

@Getter
public class RatingAboveThresholdRule implements Rule {

    Integer score;
    private final double threshold;

    public RatingAboveThresholdRule(Integer score, double threshold) {
        this.score = score;
        this.threshold = threshold;
    }

    public Boolean evaluate(Merchant merchant) {
        return merchant.getRating() != null && merchant.getRating() >= threshold;
    }
}
