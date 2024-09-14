package com.example.LeadGenerator.Service.enrichment.ruleImpl;

import com.example.LeadGenerator.Service.enrichment.Rule;
import com.example.LeadGenerator.entity.Merchant;
import lombok.Getter;

@Getter
public class ValidContactNumberRule implements Rule {

    Integer score;

    public ValidContactNumberRule(Integer score) {
        this.score = score;
    }

    public Boolean evaluate(Merchant merchant) {
        return merchant.getContactNumber() != null && merchant.getContactNumber().matches("\\d{10}");
    }
}
