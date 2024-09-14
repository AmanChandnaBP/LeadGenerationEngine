package com.example.LeadGenerator.Service.enrichment.ruleImpl;

import com.example.LeadGenerator.Service.enrichment.Rule;
import com.example.LeadGenerator.entity.Merchant;
import lombok.Getter;

@Getter
public class BusinessStatusOperationalRule implements Rule {

    Integer score;

    public BusinessStatusOperationalRule(Integer score) {
        this.score = score;
    }

    public Boolean evaluate(Merchant merchant) {
        return "OPERATIONAL".equals(merchant.getBusinessStatus());
    }
}
